package com.trustshield.repository;

import com.trustshield.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByRiskStatus(String status);
}