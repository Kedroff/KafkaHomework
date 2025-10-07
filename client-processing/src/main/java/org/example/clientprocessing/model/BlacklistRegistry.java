package org.example.clientprocessing.model;

import enums.clientProcessing.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "blacklist_registry")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 20)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false, length = 30)
    private String documentId;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "reason", length = 128)
    private String reason;

    @Column(name = "blacklist_expiration_date")
    private LocalDateTime blacklistExpirationDate;
}
