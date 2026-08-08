package com.trustshield.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GeminiVisionService {

    public Map<String, Object> analyzeImage(String imageUrl, String productTitle,
                                             String brand, Double price, Double msrp) {
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("🖼️ Analyzing product authenticity (Fallback Mode - No Gemini)...");
        System.out.println("   📝 Product: " + productTitle);
        System.out.println("   🏷️ Brand: " + (brand != null ? brand : "Not Provided"));
        System.out.println("   💰 Price: ₹" + price);
        System.out.println("   📊 MSRP: ₹" + msrp);
        System.out.println("   🖼️ Image: " + imageUrl);
        
        int score = 0;
        List<String> reasons = new ArrayList<>();
        
        // ========================================
        // 1. PRICE CHECK
        // ========================================
        if (price != null && msrp != null && msrp > 0) {
            double ratio = price / msrp;
            System.out.println("   📈 Price Ratio: " + String.format("%.0f", ratio * 100) + "%");
            
            if (ratio < 0.3) {
                score += 45;
                reasons.add("💰 Price is only " + String.format("%.0f", ratio * 100) + "% of MSRP - VERY SUSPICIOUS");
            } else if (ratio < 0.5) {
                score += 30;
                reasons.add("💰 Price is " + String.format("%.0f", ratio * 100) + "% of MSRP - too low");
            } else if (ratio < 0.7) {
                score += 15;
                reasons.add("💰 Price is " + String.format("%.0f", ratio * 100) + "% of MSRP - lower than expected");
            } else {
                reasons.add("💰 Price is " + String.format("%.0f", ratio * 100) + "% of MSRP - reasonable");
            }
        } else {
            score += 10;
            reasons.add("⚠️ Price or MSRP missing - cannot verify pricing");
        }
        
        // ========================================
        // 2. IMAGE URL CHECK
        // ========================================
        if (imageUrl == null || imageUrl.isEmpty()) {
            score += 25;
            reasons.add("❌ No image URL provided");
        } else if (imageUrl.contains("kluniversity") || imageUrl.contains("university") ||
                   imageUrl.contains("college") || imageUrl.contains("school") ||
                   imageUrl.contains("building") || imageUrl.contains("architecture") ||
                   imageUrl.contains("example.com") || imageUrl.contains("placeholder") ||
                   imageUrl.contains("picsum.photos") || imageUrl.contains("loremflickr") ||
                   imageUrl.contains("dummyimage") || imageUrl.contains("via.placeholder")) {
            score += 40;
            reasons.add("❌ Image appears to be a placeholder, stock photo, or unrelated image");
        } else if (imageUrl.contains("unsplash") || imageUrl.contains("pexels")) {
            reasons.add("✅ Professional stock image - appears legitimate");
        } else {
            reasons.add("✅ Image URL appears valid");
        }
        
        // ========================================
        // 3. BRAND CHECK
        // ========================================
        if (brand != null && !brand.trim().isEmpty()) {
            String b = brand.toLowerCase();
            if (b.contains("rolex") || b.contains("gucci") || b.contains("louis") ||
                b.contains("prada") || b.contains("chanel") || b.contains("hermes") ||
                b.contains("cartier") || b.contains("tiffany") || b.contains("omega")) {
                score += 20;
                reasons.add("⚠️ Luxury brand detected - higher counterfeit risk");
            } else {
                reasons.add("✅ Brand: " + brand);
            }
        } else {
            score += 10;
            reasons.add("⚠️ Brand not provided - incomplete product information");
        }
        
        // ========================================
        // 4. TITLE CHECK
        // ========================================
        if (productTitle != null) {
            String t = productTitle.toLowerCase();
            if (t.contains("replica") || t.contains("fake") || t.contains("copy") ||
                t.contains("duplicate") || t.contains("knockoff") || t.contains("clone")) {
                score += 35;
                reasons.add("❌ Title contains suspicious keywords (replica/fake/copy)");
            } else {
                reasons.add("✅ Title: " + productTitle);
            }
        }
        
        // ========================================
        // 5. BONUS: Image-Product Mismatch
        // ========================================
        if (imageUrl != null && productTitle != null) {
            String url = imageUrl.toLowerCase();
            String title = productTitle.toLowerCase();
            
            // Check if image is of a building but product is a consumer item
            String[] consumerProducts = {"iphone", "watch", "shoe", "bag", "phone", "laptop", "tv", "headphone", "camera"};
            boolean isConsumer = false;
            for (String p : consumerProducts) {
                if (title.contains(p)) {
                    isConsumer = true;
                    break;
                }
            }
            
            if (isConsumer && (url.contains("building") || url.contains("architecture") ||
                               url.contains("university") || url.contains("college") ||
                               url.contains("school"))) {
                score += 15;
                reasons.add("⚠️ Image appears to be of a building/landscape, not a consumer product");
            }
        }
        
        // Cap score at 100
        score = Math.min(score, 100);
        
        // Determine status
        boolean isAuthentic = score < 40;
        String status = isAuthentic ? "✅ AUTHENTIC" : "⚠️ SUSPICIOUS";
        
        // If no issues found
        if (reasons.isEmpty()) {
            reasons.add("✅ Product appears authentic");
        }
        
        // Add AI disclaimer
        reasons.add(0, "🤖 Analyzed by AI TrustShield (Fallback Mode)");
        
        result.put("counterfeitScore", score);
        result.put("authentic", isAuthentic);
        result.put("reasons", reasons);
        
        System.out.println("   " + "=".repeat(50));
        System.out.println("📊 Counterfeit Score: " + score + "%");
        System.out.println("📊 Status: " + status);
        System.out.println("📊 Reasons:");
        for (String reason : reasons) {
            System.out.println("   • " + reason);
        }
        System.out.println("   " + "=".repeat(50));
        
        return result;
    }
}