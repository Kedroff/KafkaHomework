package org.example.accountprocessing.repository;

import org.example.accountprocessing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountId(Long accountId);
    List<Payment> findByAccountIdAndPaymentDateLessThanEqualAndExpiredFalse(Long accountId, LocalDate date);
    List<Payment> findByAccountIdAndExpiredFalseAndPayedAtIsNull(Long accountId);
}