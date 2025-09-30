package org.example.accountprocessing.kafka;

import dto.clientProcessing.ClientProductDto;
import dto.accountProcessing.CardDto;
import dto.accountProcessing.TransactionDto;
import dto.accountProcessing.PaymentDto;
import enums.accountProcessing.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.model.Account;
import org.example.accountprocessing.model.Card;
import org.example.accountprocessing.model.Payment;
import org.example.accountprocessing.model.Transaction;
import org.example.accountprocessing.repository.AccountRepository;
import org.example.accountprocessing.repository.CardRepository;
import org.example.accountprocessing.repository.PaymentRepository;
import org.example.accountprocessing.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAccountConsumer {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;

    @Value("${t1.transaction.max-per-card:10}")
    private int maxTransactionsPerCard;

    @Value("${t1.transaction.time-window-minutes:60}")
    private int timeWindowMinutes;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.client_products}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onClientProducts(@Payload List<ClientProductDto> messages,
                                 Acknowledgment ack,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            for (ClientProductDto dto : messages) {
                log.info("client_products: пришло сообщение: {}", dto);
                Account account = Account.builder()
                        .clientId(dto.getClientId())
                        .productId(dto.getProductId())
                        .balance(BigDecimal.ZERO)
                        .interestRate(BigDecimal.ZERO)
                        .isRecalc(false)
                        .cardExist(false)
                        .status(AccountStatus.ACTIVE)
                        .build();
                accountRepository.save(account);
            }
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "${t1.kafka.consumer.group-id}-cards",
            topics = "${t1.kafka.topic.client_cards}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onClientCards(@Payload List<CardDto> messages,
                              Acknowledgment ack,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            for (CardDto dto : messages) {
                log.info("client_cards: пришло сообщение: {}", dto);
                accountRepository.findById(dto.getAccountId()).ifPresentOrElse(account -> {
                    if (account.getStatus() != AccountStatus.BLOCKED) {
                        Card card = Card.builder()
                                .accountId(account.getId())
                                .cardId(dto.getCardId())
                                .paymentSystem(dto.getPaymentSystem())
                                .status(dto.getStatus())
                                .build();
                        Card saved = cardRepository.save(card);
                        log.info("карта создана: id={}, accountId={}, cardId={}", saved.getId(), saved.getAccountId(), saved.getCardId());
                    } else {
                        log.warn("счет {} заблокирован, карту не создаем", account.getId());
                    }
                }, () -> log.warn("счет не найден, карту не создаем: accountId={}", dto.getAccountId()));
            }
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(id = "${t1.kafka.consumer.group-id}-transactions",
            topics = "${t1.kafka.topic.client_transactions:client_transactions}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onClientTransactions(@Payload List<TransactionDto> messages,
                                     Acknowledgment ack,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        try {
            for (TransactionDto dto : messages) {
                log.info("client_transactions: пришла транзакция: {}, ключ сообщения: {}", dto, messageKey);

                accountRepository.findById(dto.getAccountId()).ifPresentOrElse(account -> {

                    if (account.getStatus() != AccountStatus.BLOCKED && account.getStatus() != AccountStatus.ARRESTED) {

                        if (dto.getCardId() != null && shouldFreezeTransactions(dto.getCardId())) {
                            log.warn("Превышен лимит транзакций по карте {}: больше {} за {} минут", 
                                    dto.getCardId(), maxTransactionsPerCard, timeWindowMinutes);
                            freezeAccount(account.getId());
                            return;
                        }

                        BigDecimal newBalance;
                        if (dto.getType() == enums.accountProcessing.TransactionType.DEPOSIT) {
                            newBalance = account.getBalance().add(dto.getAmount());
                            log.info("Начисление на счет {}: +{} = {}", account.getId(), dto.getAmount(), newBalance);

                            if (account.getIsRecalc()) {
                                processAutomaticPayments(account);
                            }
                        } else {
                            newBalance = account.getBalance().subtract(dto.getAmount());
                            log.info("Списание со счета {}: -{} = {}", account.getId(), dto.getAmount(), newBalance);
                        }

                        account.setBalance(newBalance);
                        accountRepository.save(account);
                        
                        log.info("Баланс счета {} обновлен: {}", account.getId(), newBalance);

                        if (account.getIsRecalc()) {
                            createPaymentSchedule(account);
                        }
                        
                    } else {
                        log.warn("Счет {} заблокирован или арестован, транзакция отклонена", account.getId());
                    }
                }, () -> {
                    log.warn("Счет не найден: accountId={}", dto.getAccountId());
                });
            }
        } finally {
            ack.acknowledge();
        }
    }
    @KafkaListener(id = "${t1.kafka.consumer.group-id}-payments",
            topics = "${t1.kafka.topic.client_payments:client_payments}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onClientPayments(@Payload List<PaymentDto> messages,
                                 Acknowledgment ack,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        try {
            for (PaymentDto dto : messages) {
                log.info("client_payments: пришел платеж: {}, ключ сообщения: {}", dto, messageKey);
                accountRepository.findById(dto.getAccountId()).ifPresentOrElse(account -> {
                    if (Boolean.TRUE.equals(account.getIsRecalc())) {
                        List<Payment> unpaid = paymentRepository.findByAccountIdAndExpiredFalseAndPayedAtIsNull(account.getId());
                        BigDecimal debt = unpaid.stream()
                                .map(Payment::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (dto.getAmount() != null && dto.getAmount().compareTo(debt) == 0) {
                            BigDecimal newBalance = account.getBalance().subtract(dto.getAmount());
                            account.setBalance(newBalance);
                            accountRepository.save(account);

                            Payment payment = Payment.builder()
                                    .accountId(account.getId())
                                    .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDate.now())
                                    .amount(dto.getAmount())
                                    .isCredit(true)
                                    .type(dto.getType())
                                    .payedAt(LocalDateTime.now())
                                    .expired(false)
                                    .build();
                            paymentRepository.save(payment);

                            unpaid.forEach(p -> p.setPayedAt(LocalDateTime.now()));
                            paymentRepository.saveAll(unpaid);
                            log.info("Платеж принят и задолженность закрыта по счету {}. Новый баланс: {}", account.getId(), newBalance);
                        } else {
                            log.warn("Платеж не равен сумме задолженности. Ожидалось: {}, пришло: {}", debt, dto.getAmount());
                        }
                    } else {
                        log.warn("Счет {} не кредитный, обработка client_payments пропущена", account.getId());
                    }
                }, () -> log.warn("Счет не найден: accountId={}", dto.getAccountId()));
            }
        } finally {
            ack.acknowledge();
        }
    }
    private void createPaymentSchedule(Account account) {
        log.info("Создание графика платежей для кредитного счета: {}", account.getId());
        BigDecimal monthlyPayment = account.getBalance()
                .multiply(account.getInterestRate())
                .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP);
        log.info("Ежемесячный платеж для счета {}: {} (баланс: {}, ставка: {}%)", 
                account.getId(), monthlyPayment, account.getBalance(), account.getInterestRate());
        for (int month = 1; month <= 12; month++) {
            Payment payment = Payment.builder()
                    .accountId(account.getId())
                    .paymentDate(LocalDate.now().plusMonths(month))
                    .amount(monthlyPayment)
                    .isCredit(true)
                    .type(enums.accountProcessing.PaymentType.MONTHLY)
                    .expired(false)
                    .build();
            paymentRepository.save(payment);
            log.info("Создан платеж {} для счета {} на дату: {}", 
                    month, account.getId(), payment.getPaymentDate());
        }
        log.info("График платежей создан для счета {}: 12 платежей по {}", account.getId(), monthlyPayment);
    }
    private boolean shouldFreezeTransactions(Long cardId) {
        LocalDateTime fromTime = LocalDateTime.now().minusMinutes(timeWindowMinutes);
        List<Transaction> recentTransactions = transactionRepository.findByCardIdAndTimestampAfter(cardId, fromTime);
        log.info("Проверка лимита для карты {}: {} транзакций за последние {} минут (лимит: {})", 
                cardId, recentTransactions.size(), timeWindowMinutes, maxTransactionsPerCard);
        return recentTransactions.size() >= maxTransactionsPerCard;
    }
    private void freezeAccount(Long accountId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            account.setStatus(AccountStatus.BLOCKED);
            accountRepository.save(account);
            log.warn("Счет {} заблокирован из-за превышения лимита транзакций", accountId);
        });
    }
    private void processAutomaticPayments(Account account) {
        log.info("Проверка автоматических платежей для кредитного счета: {}", account.getId());
        List<Payment> pendingPayments = paymentRepository.findByAccountIdAndPaymentDateLessThanEqualAndExpiredFalse(account.getId(), LocalDate.now());
        for (Payment payment : pendingPayments) {
            log.info("Обработка платежа {} на сумму {} для счета {}", 
                    payment.getId(), payment.getAmount(), account.getId());
            if (account.getBalance().compareTo(payment.getAmount()) >= 0) {
                account.setBalance(account.getBalance().subtract(payment.getAmount()));
                payment.setPayedAt(LocalDateTime.now());
                payment.setExpired(false);
                log.info("Автоматический платеж выполнен: списано {} со счета {}", 
                        payment.getAmount(), account.getId());
            } else {
                payment.setExpired(true);
                log.warn("Недостаточно средств для платежа {}: требуется {}, доступно {}", 
                        payment.getId(), payment.getAmount(), account.getBalance());
            }
        }
        if (!pendingPayments.isEmpty()) {
            accountRepository.save(account);
            paymentRepository.saveAll(pendingPayments);
            log.info("Обработано {} автоматических платежей для счета {}", 
                    pendingPayments.size(), account.getId());
        }
    }
}


