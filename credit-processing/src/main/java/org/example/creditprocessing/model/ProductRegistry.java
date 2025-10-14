package org.example.creditprocessing.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "product_registry")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "month_count", nullable = false)
    private Integer monthCount;
}
