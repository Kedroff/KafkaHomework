package org.example.accountprocessing.kafka;

import dto.clientProcessing.ClientProductDto;
import dto.accountProcessing.CardDto;
import dto.accountProcessing.TransactionDto;
import enums.accountProcessing.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.model.Account;
import org.example.accountprocessing.model.Card;
import org.example.accountprocessing.repository.AccountRepository;
import org.example.accountprocessing.repository.CardRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAccountConsumer {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

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
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            for (TransactionDto dto : messages) {
                log.info("client_transactions: пришла транзакция: {}", dto);
            }
        } finally {
            ack.acknowledge();
        }
    }
}


