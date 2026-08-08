package com.trustshield.service;

import com.trustshield.dto.RiskRequest;
import com.trustshield.dto.RiskResponse;
import com.trustshield.entity.AuditTrail;
import com.trustshield.entity.Transaction;
import com.trustshield.repository.AuditTrailRepository;
import com.trustshield.repository.TransactionRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RiskScoringService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    private final Gson gson = new Gson();

    public RiskResponse analyzeTransaction(RiskRequest request) {
        try {
            // 1. Call Python ML Model
            Map<String, Object> pythonInput = new HashMap<>();
            pythonInput.put("type", "risk");
            
            Map<String, Object> data = new HashMap<>();
            data.put("TransactionAmt", request.getOrderAmount());
            data.put("ProductCD", request.getPaymentMethod());
            data.put("card1", 1);
            data.put("card2", 1);
            data.put("card3", 1);
            data.put("card4", "Visa");
            data.put("card5", 1);
            data.put("card6", 1);
            data.put("addr1", 1);
            data.put("addr2", 1);
            data.put("P_emaildomain", "gmail.com");
            data.put("R_emaildomain", "gmail.com");
            data.put("DeviceType", "mobile");
            data.put("DeviceInfo", "iPhone");
            
            pythonInput.put("data", data);
            
            String pythonJson = gson.toJson(pythonInput);
            
            // 2. Execute Python script
            ProcessBuilder pb = new ProcessBuilder(
    "python",
    "C:/CERTIFICATIONS/ml-models/predict_service.py",
    pythonJson
);
            
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // 3. Read output
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            String line = reader.readLine();
            process.waitFor();
            
            // 4. Parse result
            Map<String, Object> result = gson.fromJson(line, Map.class);
            
            if (result.containsKey("error")) {
                // If AI fails, use fallback
                return analyzeTransactionFallback(request);
            }
            
            Integer riskScore = ((Double) result.get("score")).intValue();
            
            // 5. Determine status
            String status = riskScore > 80 ? "HIGH_RISK" : 
                           riskScore > 50 ? "MEDIUM_RISK" : "LOW_RISK";
            
            // 6. Generate reasons
            List<String> reasons = generateReasons(request, riskScore);
            reasons.add(0, "AI Model analysis completed");
            
            // 7. Determine action
            String action = riskScore > 80 ? "MANUAL_VERIFICATION" : 
                           riskScore > 50 ? "REVIEW" : "APPROVE";
            
            String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // 8. Save to database
            saveTransaction(request, riskScore, status, transactionId);
            saveAuditTrail("risk_agent_ai", transactionId, status, riskScore, reasons);
            
            // 9. Return response
            RiskResponse response = new RiskResponse();
            response.setRiskScore(riskScore);
            response.setStatus(status);
            response.setReasons(reasons);
            response.setAction(action);
            response.setTransactionId(transactionId);
            
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to rule-based if AI fails
            return analyzeTransactionFallback(request);
        }
    }

    // ===== FALLBACK: Rule-based logic (your original code) =====
    private RiskResponse analyzeTransactionFallback(RiskRequest request) {
        int riskScore = calculateRiskScore(request);
        String status = riskScore > 80 ? "HIGH_RISK" : 
                       riskScore > 50 ? "MEDIUM_RISK" : "LOW_RISK";
        List<String> reasons = generateReasons(request, riskScore);
        String action = riskScore > 80 ? "MANUAL_VERIFICATION" : 
                       riskScore > 50 ? "REVIEW" : "APPROVE";
        String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        saveTransaction(request, riskScore, status, transactionId);
        saveAuditTrail("risk_agent_fallback", transactionId, status, riskScore, reasons);
        
        RiskResponse response = new RiskResponse();
        response.setRiskScore(riskScore);
        response.setStatus(status);
        response.setReasons(reasons);
        response.setAction(action);
        response.setTransactionId(transactionId);
        
        return response;
    }

    private int calculateRiskScore(RiskRequest request) {
        int score = 0;
        if (request.getOrderAmount() > 10000) score += 20;
        if (request.getOrderAmount() > 50000) score += 20;
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) score += 30;
        if (request.getPreviousReturns() != null) {
            if (request.getPreviousReturns() > 3) score += 15;
            if (request.getPreviousReturns() > 5) score += 20;
        }
        if (request.getIpAddress() != null && request.getIpAddress().startsWith("192")) {
            score += 10;
        }
        return Math.min(score, 100);
    }

    private List<String> generateReasons(RiskRequest request, int score) {
        List<String> reasons = new ArrayList<>();
        if (score > 80) reasons.add("High risk transaction detected");
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            reasons.add("COD order - higher risk of non-payment");
        }
        if (request.getPreviousReturns() != null && request.getPreviousReturns() > 3) {
            reasons.add(request.getPreviousReturns() + " previous returns");
        }
        if (request.getOrderAmount() > 50000) {
            reasons.add("High value order: ₹" + request.getOrderAmount());
        }
        if (reasons.isEmpty()) {
            reasons.add("Transaction appears normal");
        }
        return reasons;
    }

    private void saveTransaction(RiskRequest request, int riskScore, String status, String transactionId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setCustomerId(request.getCustomerId());
        transaction.setOrderAmount(java.math.BigDecimal.valueOf(request.getOrderAmount()));
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setPreviousReturns(request.getPreviousReturns());
        transaction.setDeviceId(request.getDeviceId());
        transaction.setIpAddress(request.getIpAddress());
        transaction.setRiskScore(riskScore);
        transaction.setRiskStatus(status);
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private void saveAuditTrail(String agent, String entityId, String decision, int score, List<String> reasons) {
        AuditTrail audit = new AuditTrail();
        audit.setAuditId("AUD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        audit.setAgentName(agent);
        audit.setEntityId(entityId);
        audit.setDecision(decision);
        audit.setConfidenceScore(score);
        audit.setReasons(String.join("; ", reasons));
        audit.setCreatedAt(LocalDateTime.now());
        auditTrailRepository.save(audit);
    }
}