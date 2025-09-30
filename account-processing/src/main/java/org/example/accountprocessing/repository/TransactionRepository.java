package org.example.accountprocessing.repository;

import org.example.accountprocessing.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardIdAndTimestampAfter(Long cardId, LocalDateTime fromTime);
    List<Transaction> findByAccountId(Long accountId);
}