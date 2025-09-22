package org.example.clientprocessing.model;

import enums.clientProcessing.ClientProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "client_products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProduct extends AbstractPersistable<Long> {

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientProductStatus status;
}
