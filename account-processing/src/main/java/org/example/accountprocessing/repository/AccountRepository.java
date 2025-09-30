package org.example.accountprocessing.repository;

import org.example.accountprocessing.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClientId(Long clientId);

    Optional<Account> findByProductId(Long productId);
}
