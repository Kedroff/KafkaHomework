package org.example.clientprocessing.repository;

import enums.clientProcessing.DocumentType;
import org.example.clientprocessing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);

    boolean existsByDocumentTypeAndDocumentId(DocumentType documentType, String documentId);
}
