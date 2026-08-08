package com.trustshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail")
public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String auditId;
    private String agentName;
    private String entityId;
    private String decision;
    private Integer confidenceScore;
    private String reasons;
    private LocalDateTime createdAt;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getAuditId() { return auditId; }
    public String getAgentName() { return agentName; }
    public String getEntityId() { return entityId; }
    public String getDecision() { return decision; }
    public Integer getConfidenceScore() { return confidenceScore; }
    public String getReasons() { return reasons; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setAuditId(String auditId) { this.auditId = auditId; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public void setDecision(String decision) { this.decision = decision; }
    public void setConfidenceScore(Integer confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setReasons(String reasons) { this.reasons = reasons; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}