package org.example.clientprocessing.model;

import enums.clientProcessing.ProductKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_key", nullable = false)
    private ProductKey key;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "product_id", nullable = false, length = 30, unique = true)
    private String productId;

    @PostPersist
    private void generateProductId() {
        if (id != null && key != null) {
            this.productId = key.name() + id;
        }
    }
}
