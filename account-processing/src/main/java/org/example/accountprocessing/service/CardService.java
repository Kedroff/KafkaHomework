package org.example.accountprocessing.service;

import dto.accountProcessing.CardDto;
import java.util.List;

public interface CardService {
    CardDto createCard(CardDto dto);
    CardDto getCardById(Long id);
    List<CardDto> getAllCards();
    List<CardDto> getCardsByAccountId(Long accountId);
    CardDto updateCard(Long id, CardDto dto);
    void deleteCard(Long id);
}
