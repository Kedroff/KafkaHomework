package org.example.clientprocessing.model;

import enums.clientProcessing.DocumentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends AbstractPersistable<Long> {

    @Column(name = "client_id", nullable = false, unique = true, length = 12)
    private String clientId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "middle_name", length = 20)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false, length = 30)
    private String documentId;

    @Column(name = "document_prefix", length = 10)
    private String documentPrefix;

    @Column(name = "document_suffix", length = 10)
    private String documentSuffix;
}
