package com.trustshield.dto;

public class ProductRequest {
    private String productId;
    private String sellerId;
    private String title;
    private String description;
    private Double price;
    private String brand;
    private String imageUrl;
    private Double msrp;

    // ===== GETTERS =====
    public String getProductId() { return productId; }
    public String getSellerId() { return sellerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getBrand() { return brand; }
    public String getImageUrl() { return imageUrl; }
    public Double getMsrp() { return msrp; }

    // ===== SETTERS =====
    public void setProductId(String productId) { this.productId = productId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMsrp(Double msrp) { this.msrp = msrp; }
}