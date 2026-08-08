package com.trustshield.repository;

import com.trustshield.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long> {
    List<AuditTrail> findTop10ByOrderByCreatedAtDesc();
}