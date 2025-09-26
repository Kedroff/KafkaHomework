package org.example.accountprocessing.service.impl;

import dto.accountProcessing.CardDto;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.service.CardService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    @Override
    public CardDto createCard(CardDto dto) {
        log.info("создание карты: {}", dto);
        return dto;
    }

    @Override
    public CardDto getCardById(Long id) {
        log.info("получение карты по id={}", id);
        return null;
    }

    @Override
    public List<CardDto> getAllCards() {
        log.info("получение всех карт");
        return Collections.emptyList();
    }

    @Override
    public List<CardDto> getCardsByAccountId(Long accountId) {
        log.info("получение карт по accountId={}", accountId);
        return Collections.emptyList();
    }

    @Override
    public CardDto updateCard(Long id, CardDto dto) {
        log.info("обновление карты id={} данными {}", id, dto);
        return dto;
    }

    @Override
    public void deleteCard(Long id) {
        log.info("удаление карты id={}", id);
    }
}
