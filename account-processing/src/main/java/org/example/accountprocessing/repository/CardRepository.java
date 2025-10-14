package org.example.accountprocessing.repository;

import org.example.accountprocessing.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardId(String cardId);

    List<Card> findByAccountId(Long accountId);
}
