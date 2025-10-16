package org.example.creditprocessing.kafka;

import dto.clientProcessing.ClientProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.creditprocessing.model.PaymentRegistry;
import org.example.creditprocessing.model.ProductRegistry;
import org.example.creditprocessing.repository.PaymentRegistryRepository;
import org.example.creditprocessing.repository.ProductRegistryRepository;
import org.example.creditprocessing.service.CreditDecisionService;
import org.example.creditprocessing.service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCreditConsumer {

    private final CreditDecisionService creditDecisionService;
    private final PaymentScheduleService paymentScheduleService;
    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    @Value("${t1.credit.limit}")
    private BigDecimal creditLimit;
    @Value("${t1.credit.default-principal}")
    private BigDecimal defaultPrincipal;
    @Value("${t1.credit.default-annual-rate}")
    private BigDecimal defaultAnnualRate;
    @Value("${t1.credit.default-months}")
    private Integer defaultMonths;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.client_credit_products}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onClientCreditProducts(@Payload List<ClientProductDto> messages,
                                       Acknowledgment ack,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            for (ClientProductDto dto : messages) {
                log.info("client_credit_products: пришло сообщение: {}", dto);

                boolean approved = creditDecisionService.isApproved(dto.getClientId(), dto);
                if (!approved) {
                    log.info("кредит отклонён, clientId={}", dto.getClientId());
                    continue;
                }

                ProductRegistry pr = ProductRegistry.builder()
                        .clientId(dto.getClientId())
                        .accountId(null)
                        .productId(dto.getProductId())
                        .interestRate(defaultAnnualRate)
                        .openDate(LocalDate.now())
                        .monthCount(defaultMonths)
                        .build();
                pr = productRegistryRepository.save(pr);

                List<PaymentRegistry> schedule = paymentScheduleService.generateAnnuitySchedule(
                        defaultPrincipal, defaultAnnualRate, defaultMonths
                );
                Long prId = pr.getId();
                schedule.forEach(p -> p.setProductRegistryId(prId));
                paymentRegistryRepository.saveAll(schedule);
                log.info("кредит одобрен и открыт: clientId={}, productRegistryId={}", dto.getClientId(), prId);
            }
        } finally {
            ack.acknowledge();
        }
    }
}


