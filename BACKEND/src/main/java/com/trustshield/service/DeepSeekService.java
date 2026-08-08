package com.trustshield.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DeepSeekService {

    // ============================================================
    // ANALYZE REVIEW - FREE MODE (No API calls)
    // ============================================================
    public Map<String, Object> analyzeReview(String reviewText, Integer rating, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("🤖 Analyzing review with AI engine (Free Mode)...");
        System.out.println("   📝 Review: " + reviewText);
        System.out.println("   ⭐ Rating: " + rating + "/5");
        
        int score = 0;
        List<String> reasons = new ArrayList<>();
        String text = reviewText.toLowerCase();
        
        // 1. Check for spammy patterns
        long exclamationCount = reviewText.chars().filter(ch -> ch == '!').count();
        if (exclamationCount > 3) {
            score += 20;
            reasons.add("⚠️ Excessive exclamation marks (" + exclamationCount + " times)");
        }
        
        // 2. ALL CAPS detection
        long upperCount = reviewText.chars().filter(Character::isUpperCase).count();
        if (reviewText.length() > 0 && (double) upperCount / reviewText.length() > 0.4) {
            score += 15;
            reasons.add("⚠️ Excessive CAPITAL letters (bot pattern)");
        }
        
        // 3. Repetitive words
        String[] words = text.split("\\s+");
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            if (word.length() > 3) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 3) {
                score += 10;
                reasons.add("⚠️ Repetitive word: '" + entry.getKey() + "' (" + entry.getValue() + " times)");
                break;
            }
        }
        
        // 4. Spam keywords
        String[] spamKeywords = {"amazing", "best", "excellent", "great", "awesome", 
                                  "perfect", "wonderful", "superb", "fantastic", "outstanding"};
        int spamCount = 0;
        for (String keyword : spamKeywords) {
            if (text.contains(keyword)) {
                spamCount++;
            }
        }
        if (spamCount > 3) {
            score += 15;
            reasons.add("⚠️ " + spamCount + " spammy marketing keywords");
        }
        
        // 5. Short review with 5 stars
        if (reviewText.length() < 40 && rating == 5) {
            score += 15;
            reasons.add("⚠️ Short review with 5-star rating");
        }
        
        // 6. Bot patterns
        if (text.matches(".*[!]{2,}.*")) {
            score += 10;
            reasons.add("⚠️ Multiple exclamation marks together");
        }
        
        // 7. No specific details with 5 stars
        if (rating == 5 && !text.contains("delivery") && !text.contains("quality") && !text.contains("price")) {
            score += 10;
            reasons.add("⚠️ 5-star with no specific product details");
        }
        
        // 8. Genuine indicators (reduce score)
        if (text.contains("delivery") || text.contains("shipping") || 
            text.contains("quality") || text.contains("price") ||
            text.contains("value") || text.contains("recommend")) {
            score -= 10;
        }
        
        // 9. Specific details (reduce score)
        if (reviewText.length() > 100) {
            score -= 10;
        }
        
        // Cap score
        score = Math.max(0, Math.min(score, 100));
        
        // Determine status
        boolean isFake = score > 60;
        String confidence = score > 70 ? "high" : (score > 50 ? "medium" : "low");
        
        // Add AI label
        reasons.add(0, "🤖 Analyzed by AI TrustShield");
        
        result.put("fraudScore", score);
        result.put("isFake", isFake);
        result.put("confidence", confidence);
        result.put("reasons", reasons);
        
        System.out.println("📊 Fraud Score: " + score + "%");
        System.out.println("📊 Status: " + (isFake ? "⚠️ FAKE DETECTED" : "✅ GENUINE"));
        System.out.println("📊 Reasons: " + reasons);
        
        return result;
    }

    // ============================================================
    // ANALYZE PRODUCT - FREE MODE
    // ============================================================
    public Map<String, Object> analyzeProduct(String title, String description, 
                                               String brand, Double price, Double msrp) {
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("🤖 Analyzing product with AI engine (Free Mode)...");
        System.out.println("   📝 Title: " + title);
        System.out.println("   🏷️ Brand: " + brand);
        System.out.println("   💰 Price: ₹" + price);
        System.out.println("   📊 MSRP: ₹" + msrp);
        
        int score = 0;
        List<String> reasons = new ArrayList<>();
        
        // 1. Price check
        if (price != null && msrp != null && msrp > 0) {
            double ratio = price / msrp;
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
                reasons.add("✅ Price is " + String.format("%.0f", ratio * 100) + "% of MSRP - reasonable");
            }
        }
        
        // 2. Brand check
        if (brand != null && !brand.isEmpty()) {
            String b = brand.toLowerCase();
            if (b.contains("rolex") || b.contains("gucci") || b.contains("louis") ||
                b.contains("prada") || b.contains("chanel") || b.contains("hermes") ||
                b.contains("cartier") || b.contains("tiffany") || b.contains("omega")) {
                score += 20;
                reasons.add("⚠️ Luxury brand detected - higher counterfeit risk");
            }
        } else {
            score += 10;
            reasons.add("⚠️ Brand not provided - incomplete information");
        }
        
        // 3. Title/Description check
        String text = (title + " " + (description != null ? description : "")).toLowerCase();
        if (text.contains("replica") || text.contains("fake") || text.contains("copy") ||
            text.contains("duplicate") || text.contains("knockoff") || text.contains("clone")) {
            score += 30;
            reasons.add("❌ Contains suspicious keywords (replica/fake/copy)");
        }
        
        // 4. Description quality
        if (description != null && description.length() < 30) {
            score += 10;
            reasons.add("⚠️ Very short description - may be copied");
        }
        
        // Add AI label
        reasons.add(0, "🤖 Analyzed by AI TrustShield");
        
        // Cap score
        score = Math.max(0, Math.min(score, 100));
        
        boolean isCounterfeit = score > 60;
        
        result.put("counterfeitScore", score);
        result.put("isCounterfeit", isCounterfeit);
        result.put("authentic", !isCounterfeit);
        result.put("reasons", reasons);
        
        System.out.println("📊 Counterfeit Score: " + score + "%");
        System.out.println("📊 Status: " + (isCounterfeit ? "⚠️ COUNTERFEIT DETECTED" : "✅ AUTHENTIC"));
        
        return result;
    }
}