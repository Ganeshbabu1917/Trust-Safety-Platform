package com.trustshield.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String customerId;
    private BigDecimal orderAmount;
    private String paymentMethod;
    private Integer previousReturns;
    private String deviceId;
    private String ipAddress;
    private Integer riskScore;
    private String riskStatus;
    private String riskReasons;
    private LocalDateTime createdAt;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public Integer getPreviousReturns() { return previousReturns; }
    public String getDeviceId() { return deviceId; }
    public String getIpAddress() { return ipAddress; }
    public Integer getRiskScore() { return riskScore; }
    public String getRiskStatus() { return riskStatus; }
    public String getRiskReasons() { return riskReasons; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPreviousReturns(Integer previousReturns) { this.previousReturns = previousReturns; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public void setRiskStatus(String riskStatus) { this.riskStatus = riskStatus; }
    public void setRiskReasons(String riskReasons) { this.riskReasons = riskReasons; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}