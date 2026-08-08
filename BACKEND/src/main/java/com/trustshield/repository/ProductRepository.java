package com.trustshield.repository;

import com.trustshield.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySellerId(String sellerId);
    List<Product> findByAuthenticityStatus(String status);
}