package org.example.accountprocessing.service.impl;

import dto.accountProcessing.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.example.accountprocessing.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionDto createTransaction(TransactionDto dto) {
        log.info("создание транзакции: {}", dto);
        return dto;
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        log.info("получение транзакции по id={}", id);
        return null;
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        log.info("получение всех транзакций");
        return Collections.emptyList();
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        log.info("получение транзакций по accountId={}", accountId);
        return Collections.emptyList();
    }
}
