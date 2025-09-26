package org.example.clientprocessing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaClientProducer {

    @Qualifier("clientKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTo(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

    public void sendDefault(Object payload) {
        kafkaTemplate.sendDefault(UUID.randomUUID().toString(), payload);
    }
}


