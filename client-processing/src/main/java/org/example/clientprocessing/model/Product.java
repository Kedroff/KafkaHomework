package org.example.clientprocessing.model;

import enums.clientProcessing.ProductKey;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AbstractPersistable<Long> {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)            // подумал сделать проверку на (DC, CC, AC, IPO, PC, PENS, NS, INS, BS) через
    @Column(name = "product_key", nullable = false) // pattern, но ты говорил не оверинжинирить, поэтому вынесу в dto
    private ProductKey key;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "product_id", nullable = false, length = 30, unique = true)
    private String productId;

    @Override
    public Long getId() {
        return super.getId();
    }

    @PrePersist
    @PreUpdate
    private void generateProductId() {
        if (getId() != null && key != null) {
            this.productId = key.name() + getId();
        }
    }
}
