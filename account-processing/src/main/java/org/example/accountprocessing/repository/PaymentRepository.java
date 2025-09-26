package org.example.accountprocessing.repository;

import org.example.accountprocessing.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountId(Long accountId);

    List<Payment> findByPaymentDate(LocalDate date);
}
