package com.trustshield.dto;

import java.util.List;

public class ReviewResponse {
    private Integer fraudScore;
    private String status;
    private List<String> reasons;
    private String reviewId;

    // ===== GETTERS =====
    public Integer getFraudScore() {
        return fraudScore;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public String getReviewId() {
        return reviewId;
    }

    // ===== SETTERS =====
    public void setFraudScore(Integer fraudScore) {
        this.fraudScore = fraudScore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}