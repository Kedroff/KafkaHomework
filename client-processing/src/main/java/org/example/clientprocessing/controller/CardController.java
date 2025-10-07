package org.example.clientprocessing.controller;

import dto.accountProcessing.CardDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.clientprocessing.kafka.KafkaClientProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final KafkaClientProducer kafkaClientProducer;

    @Value("${t1.kafka.topic.client_cards}")
    private String topicClientCards;

    @PostMapping
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardDto cardDto) {
        kafkaClientProducer.sendTo(topicClientCards, cardDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}


