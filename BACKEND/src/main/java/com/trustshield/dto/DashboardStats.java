package com.trustshield.dto;

public class DashboardStats {
    private Integer totalOrders;
    private Integer fraudOrders;
    private Integer counterfeitListings;
    private Integer fakeReviews;
    private Double todaySavings;

    // ===== GETTERS =====
    public Integer getTotalOrders() {
        return totalOrders;
    }

    public Integer getFraudOrders() {
        return fraudOrders;
    }

    public Integer getCounterfeitListings() {
        return counterfeitListings;
    }

    public Integer getFakeReviews() {
        return fakeReviews;
    }

    public Double getTodaySavings() {
        return todaySavings;
    }

    // ===== SETTERS =====
    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setFraudOrders(Integer fraudOrders) {
        this.fraudOrders = fraudOrders;
    }

    public void setCounterfeitListings(Integer counterfeitListings) {
        this.counterfeitListings = counterfeitListings;
    }

    public void setFakeReviews(Integer fakeReviews) {
        this.fakeReviews = fakeReviews;
    }

    public void setTodaySavings(Double todaySavings) {
        this.todaySavings = todaySavings;
    }
}