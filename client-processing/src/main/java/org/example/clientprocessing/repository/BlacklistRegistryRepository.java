package org.example.clientprocessing.repository;

import enums.clientProcessing.DocumentType;
import org.example.clientprocessing.model.BlacklistRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    Optional<BlacklistRegistry> findByDocumentTypeAndDocumentId(DocumentType documentType, String documentId);
}
