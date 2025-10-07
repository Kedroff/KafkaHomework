package org.example.clientprocessing.aop;

import annotations.LogDatasourceError;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ServiceLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.clientprocessing.model.ErrorLog;
import org.example.clientprocessing.repository.ErrorLogRepository;
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
public class LogDatasourceErrorAspect {

    @Qualifier("clientKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;

    @Value("${t1.kafka.topic.service_logs:service_logs}")
    private String serviceLogsTopic;

    @Value("${t1.service.name:client-processing}")
    private String serviceName;

    @AfterThrowing(pointcut = "@annotation(LogDatasourceError)", throwing = "ex")
    public void onException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        ServiceLogMessage payload = ServiceLogMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .methodSignature(sig.toLongString())
                .exceptionText(ex.getMessage())
                .stacktrace(stackTraceToString(ex))
                .methodArgs(Arrays.toString(joinPoint.getArgs()))
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            ProducerRecord<String, Object> record = new ProducerRecord<>(serviceLogsTopic, serviceName, json);
            record.headers().add(new RecordHeader("type", "ERROR".getBytes(StandardCharsets.UTF_8)));
            record.headers().add(new RecordHeader("value", "ERROR".getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record);
        } catch (Exception sendEx) {

            ErrorLog toSave = ErrorLog.builder()
                    .createdAt(LocalDateTime.now())
                    .serviceName(serviceName)
                    .type("ERROR")
                    .methodSignature(((MethodSignature) joinPoint.getSignature()).toLongString())
                    .exceptionText(ex.getMessage())
                    .stacktrace(stackTraceToString(ex))
                    .methodArgs(Arrays.toString(joinPoint.getArgs()))
                    .build();
            try {
                errorLogRepository.save(toSave);
            } catch (Exception dbEx) {
                log.error("Не удалось отправить в Kafka и сохранить в БД: {} / {}", sendEx.getMessage(), dbEx.getMessage());
            }
        }

        log.error("[{}] Ошибка БД в {}: {}", serviceName, ((MethodSignature) joinPoint.getSignature()).toShortString(), ex.getMessage(), ex);
    }

    private String stackTraceToString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el).append('\n');
        }
        return sb.toString();
    }
}
