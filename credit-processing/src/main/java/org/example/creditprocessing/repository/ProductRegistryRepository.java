package org.example.creditprocessing.repository;

import org.example.creditprocessing.model.ProductRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
    List<ProductRegistry> findByClientId(Long clientId);

    List<ProductRegistry> findByAccountId(Long accountId);

    ProductRegistry findByProductId(Long productId);
}
