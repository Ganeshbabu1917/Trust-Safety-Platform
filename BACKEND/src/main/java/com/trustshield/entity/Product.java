package com.trustshield.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String sellerId;
    private String title;
    private String description;
    private BigDecimal price;
    private String brand;
    private String imageUrl;
    private BigDecimal msrp;
    private Integer counterfeitScore;
    private String authenticityStatus;
    private String authenticityReasons;
    private String listingStatus;
    private LocalDateTime createdAt;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getProductId() { return productId; }
    public String getSellerId() { return sellerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getBrand() { return brand; }
    public String getImageUrl() { return imageUrl; }
    public BigDecimal getMsrp() { return msrp; }
    public Integer getCounterfeitScore() { return counterfeitScore; }
    public String getAuthenticityStatus() { return authenticityStatus; }
    public String getAuthenticityReasons() { return authenticityReasons; }
    public String getListingStatus() { return listingStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMsrp(BigDecimal msrp) { this.msrp = msrp; }
    public void setCounterfeitScore(Integer counterfeitScore) { this.counterfeitScore = counterfeitScore; }
    public void setAuthenticityStatus(String authenticityStatus) { this.authenticityStatus = authenticityStatus; }
    public void setAuthenticityReasons(String authenticityReasons) { this.authenticityReasons = authenticityReasons; }
    public void setListingStatus(String listingStatus) { this.listingStatus = listingStatus; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}