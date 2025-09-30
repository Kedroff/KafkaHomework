package org.example.accountprocessing.service;

import dto.accountProcessing.TransactionDto;
import java.util.List;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto dto);
    TransactionDto getTransactionById(Long id);
    List<TransactionDto> getAllTransactions();
    List<TransactionDto> getTransactionsByAccountId(Long accountId);
}
