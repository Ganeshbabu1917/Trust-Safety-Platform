package com.trustshield.service;

import com.trustshield.dto.ProductRequest;
import com.trustshield.dto.ProductResponse;
import com.trustshield.entity.AuditTrail;
import com.trustshield.entity.Product;
import com.trustshield.repository.AuditTrailRepository;
import com.trustshield.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthenticityService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    // ============================================================
    // MAIN METHOD: Check Product Authenticity - FAST
    // ============================================================
    public ProductResponse checkAuthenticity(ProductRequest request) {
        
        // 1. Calculate counterfeit score (FAST - no external calls)
        int counterfeitScore = calculateCounterfeitScore(request);

        // 2. Determine status
        String status = counterfeitScore > 75 ? "FLAG_FOR_REVIEW" :
                        counterfeitScore > 50 ? "REVIEW" : "APPROVED";

        // 3. Generate reasons
        List<String> reasons = generateReasons(request, counterfeitScore);

        // 4. Generate product ID
        String productId = "PRD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 5. Save to database (FAST)
        saveProduct(request, counterfeitScore, status, productId);
        saveAuditTrail("authenticity_agent", productId, status, counterfeitScore, reasons);

        // 6. Return response
        ProductResponse response = new ProductResponse();
        response.setCounterfeitScore(counterfeitScore);
        response.setStatus(status);
        response.setReasons(reasons);
        response.setProductId(productId);

        return response;
    }

    // ============================================================
    // CALCULATE COUNTERFEIT SCORE - FAST (No API calls)
    // ============================================================
    private int calculateCounterfeitScore(ProductRequest request) {
        int score = 0;

        // 1. Price check (FAST)
        if (request.getMsrp() != null && request.getMsrp() > 0 && request.getPrice() != null) {
            double ratio = request.getPrice() / request.getMsrp();
            if (ratio < 0.3) score += 45;
            else if (ratio < 0.5) score += 30;
            else if (ratio < 0.7) score += 15;
            else if (ratio < 0.9) score += 5;
        }

        // 2. Image URL check (FAST - no network calls)
        if (request.getImageUrl() != null) {
            String url = request.getImageUrl().toLowerCase();
            if (url.contains("kluniversity") || url.contains("university") ||
                url.contains("college") || url.contains("school") ||
                url.contains("building") || url.contains("architecture") ||
                url.contains("example.com") || url.contains("placeholder") ||
                url.contains("picsum.photos") || url.contains("dummyimage")) {
                score += 40;
            }
        } else {
            score += 20;
        }

        // 3. Brand check (FAST)
        if (request.getBrand() != null && !request.getBrand().isEmpty()) {
            String brand = request.getBrand().toLowerCase();
            if (brand.contains("rolex") || brand.contains("gucci") || brand.contains("louis") ||
                brand.contains("prada") || brand.contains("chanel") || brand.contains("hermes") ||
                brand.contains("cartier") || brand.contains("tiffany")) {
                score += 20;
            }
        } else {
            score += 10;
        }

        // 4. Title/Description check (FAST)
        String text = (request.getTitle() + " " + (request.getDescription() != null ? request.getDescription() : "")).toLowerCase();
        if (text.contains("replica") || text.contains("fake") || text.contains("copy") ||
            text.contains("duplicate") || text.contains("knockoff")) {
            score += 30;
        }

        // 5. Image-Product mismatch (FAST)
        if (request.getImageUrl() != null && request.getTitle() != null) {
            String url = request.getImageUrl().toLowerCase();
            String title = request.getTitle().toLowerCase();
            if ((url.contains("building") || url.contains("architecture") || url.contains("university")) &&
                (title.contains("iphone") || title.contains("watch") || title.contains("phone"))) {
                score += 15;
            }
        }

        return Math.min(score, 100);
    }

    // ============================================================
    // GENERATE REASONS - FAST
    // ============================================================
    private List<String> generateReasons(ProductRequest request, int score) {
        List<String> reasons = new ArrayList<>();
        
        // Score-based reason
        if (score > 75) {
            reasons.add("🔴 High counterfeit probability");
        } else if (score > 50) {
            reasons.add("🟡 Suspicious patterns detected");
        } else {
            reasons.add("🟢 Product appears authentic");
        }

        // Image URL
        if (request.getImageUrl() != null) {
            String url = request.getImageUrl().toLowerCase();
            if (url.contains("kluniversity") || url.contains("university") || url.contains("building")) {
                reasons.add("❌ Image appears to be a building/landscape, not a product");
            }
        } else {
            reasons.add("⚠️ No image provided");
        }

        // Brand
        if (request.getBrand() == null || request.getBrand().isEmpty()) {
            reasons.add("⚠️ Brand not provided");
        }

        // Title
        String title = request.getTitle().toLowerCase();
        if (title.contains("replica") || title.contains("fake") || title.contains("copy")) {
            reasons.add("❌ Title contains suspicious keywords");
        }

        // Price
        if (request.getMsrp() != null && request.getMsrp() > 0 && request.getPrice() != null) {
            double ratio = request.getPrice() / request.getMsrp();
            if (ratio < 0.5) {
                reasons.add("💰 Price too low (" + String.format("%.0f", ratio * 100) + "% of MSRP)");
            }
        }

        return reasons;
    }

    // ============================================================
    // SAVE PRODUCT - FAST
    // ============================================================
    private void saveProduct(ProductRequest request, int score, String status, String productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setSellerId(request.getSellerId());
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        if (request.getPrice() != null) {
            product.setPrice(java.math.BigDecimal.valueOf(request.getPrice()));
        }
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        if (request.getMsrp() != null) {
            product.setMsrp(java.math.BigDecimal.valueOf(request.getMsrp()));
        }
        product.setCounterfeitScore(score);
        product.setAuthenticityStatus(status);
        product.setListingStatus(status.equals("APPROVED") ? "PUBLISHED" : "FLAGGED");
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    // ============================================================
    // SAVE AUDIT TRAIL - FAST
    // ============================================================
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