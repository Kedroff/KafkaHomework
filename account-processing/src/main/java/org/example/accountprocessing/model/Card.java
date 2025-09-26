package org.example.accountprocessing.model;

import enums.accountProcessing.CardPaymentSystem;
import enums.accountProcessing.CardStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system", nullable = false)
    private CardPaymentSystem paymentSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;
}
