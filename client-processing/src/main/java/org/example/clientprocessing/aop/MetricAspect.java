package org.example.clientprocessing.aop;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.HttpLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    @Qualifier("clientKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${t1.kafka.topic.service_logs:service_logs}")
    private String serviceLogsTopic;

    @Value("${t1.service.name:client-processing}")
    private String serviceName;

    @Value("${t1.metric.limit-ms:500}")
    private long limitMs;

    @Around("@annotation(annotations.Metric)")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;

        if (elapsed > limitMs) {
            MethodSignature sig = (MethodSignature) pjp.getSignature();
            HttpLogMessage payload = HttpLogMessage.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .methodSignature(sig.toLongString())
                    .uri("-")
                    .parameters(Arrays.toString(pjp.getArgs()))
                    .body("timeMs=" + elapsed)
                    .build();
            try {
                String json = objectMapper.writeValueAsString(payload);
                ProducerRecord<String, Object> record = new ProducerRecord<>(serviceLogsTopic, serviceName, json);
                record.headers().add(new RecordHeader("type", "WARNING".getBytes(StandardCharsets.UTF_8)));
                kafkaTemplate.send(record);
                log.warn("[{}] Превышение лимита времени: {} ms в {}", serviceName, elapsed, sig.toShortString());
            } catch (Exception ex) {
                log.error("Не удалось отправить метрику в Kafka: {}", ex.getMessage());
            }
        }
        return result;
    }
}
