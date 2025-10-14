package org.example.clientprocessing.aop;

import annotations.HttpIncomeRequestLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.HttpLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HttpIncomeRequestLogAspect {

    @Qualifier("clientKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${t1.kafka.topic.service_logs:service_logs}")
    private String serviceLogsTopic;

    @Value("${t1.service.name:client-processing}")
    private String serviceName;

    @Before("@annotation(HttpIncomeRequestLog)")
    public void before(JoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        HttpLogMessage payload = HttpLogMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .methodSignature(sig.toLongString())
                .uri(extractUri(joinPoint.getArgs()))
                .parameters(Arrays.toString(joinPoint.getArgs()))
                .body(extractBody(joinPoint.getArgs()))
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            ProducerRecord<String, Object> record = new ProducerRecord<>(serviceLogsTopic, serviceName, json);
            record.headers().add(new RecordHeader("type", "INFO".getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record);
        } catch (Exception ex) {
            log.error("Не удалось отправить HTTP income лог в Kafka: {}", ex.getMessage());
        }

        log.info("[{}] HTTP income запрос: {}", serviceName, sig.toShortString());
    }

    private String extractUri(Object[] args) {

        for (Object arg : args) {
            if (arg instanceof String && ((String) arg).startsWith("http")) {
                return (String) arg;
            }
        }
        return "unknown";
    }

    private String extractBody(Object[] args) {

        for (Object arg : args) {
            if (arg != null && !(arg instanceof String)) {
                return arg.toString();
            }
        }
        return "no body";
    }
}
