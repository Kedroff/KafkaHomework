package org.example.creditprocessing.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payment_registry")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRegistry extends AbstractPersistable<Long> {

    @Column(name = "product_registry_id", nullable = false)
    private Long productRegistryId;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal debtAmount;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "payment_expiration_date", nullable = false)
    private LocalDate paymentExpirationDate;
}
