package com.trustshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewId;
    private Long productId;
    private String userId;
    private Integer rating;
    private String reviewText;
    private Integer fraudScore;
    private String moderationStatus;
    private String fraudReasons;
    private LocalDateTime createdAt;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getReviewId() { return reviewId; }
    public Long getProductId() { return productId; }
    public String getUserId() { return userId; }
    public Integer getRating() { return rating; }
    public String getReviewText() { return reviewText; }
    public Integer getFraudScore() { return fraudScore; }
    public String getModerationStatus() { return moderationStatus; }
    public String getFraudReasons() { return fraudReasons; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public void setFraudScore(Integer fraudScore) { this.fraudScore = fraudScore; }
    public void setModerationStatus(String moderationStatus) { this.moderationStatus = moderationStatus; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}