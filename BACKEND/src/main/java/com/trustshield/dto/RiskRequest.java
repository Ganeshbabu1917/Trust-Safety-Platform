package com.trustshield.dto;

public class RiskRequest {
    private String customerId;
    private Double orderAmount;
    private String paymentMethod;
    private Integer previousReturns;
    private String deviceId;
    private String ipAddress;

    // ===== GETTERS =====
    public String getCustomerId() {
        return customerId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Integer getPreviousReturns() {
        return previousReturns;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    // ===== SETTERS =====
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPreviousReturns(Integer previousReturns) {
        this.previousReturns = previousReturns;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}