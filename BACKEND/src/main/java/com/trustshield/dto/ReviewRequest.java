package com.trustshield.dto;

public class ReviewRequest {
    private String reviewId;
    private Long productId;
    private String userId;
    private Integer rating;
    private String reviewText;

    // ===== GETTERS =====
    public String getReviewId() { return reviewId; }
    public Long getProductId() { return productId; }
    public String getUserId() { return userId; }
    public Integer getRating() { return rating; }
    public String getReviewText() { return reviewText; }

    // ===== SETTERS =====
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}