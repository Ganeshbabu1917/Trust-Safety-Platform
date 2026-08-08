package com.trustshield.dto;

import java.util.List;

public class ProductResponse {
    private Integer counterfeitScore;
    private String status;
    private List<String> reasons;
    private String productId;

    // ===== GETTERS =====
    public Integer getCounterfeitScore() {
        return counterfeitScore;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String getProductId() {
        return productId;
    }

    // ===== SETTERS =====
    public void setCounterfeitScore(Integer counterfeitScore) {
        this.counterfeitScore = counterfeitScore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}