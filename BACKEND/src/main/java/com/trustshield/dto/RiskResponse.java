package com.trustshield.dto;

import java.util.List;

public class RiskResponse {
    private Integer riskScore;
    private String status;
    private List<String> reasons;
    private String action;
    private String transactionId;

    // ===== GETTERS =====
    public Integer getRiskScore() {
        return riskScore;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String getAction() {
        return action;
    }

    public String getTransactionId() {
        return transactionId;
    }

    // ===== SETTERS =====
    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}