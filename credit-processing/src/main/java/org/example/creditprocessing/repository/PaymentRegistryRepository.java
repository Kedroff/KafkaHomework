package org.example.creditprocessing.repository;

import org.example.creditprocessing.model.PaymentRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {
    List<PaymentRegistry> findByProductRegistryId(Long productRegistryId);

    List<PaymentRegistry> findByExpiredTrue();

    List<PaymentRegistry> findByPaymentDate(LocalDate paymentDate);
}
