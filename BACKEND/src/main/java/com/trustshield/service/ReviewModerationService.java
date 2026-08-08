package com.trustshield.service;

import com.trustshield.dto.ReviewRequest;
import com.trustshield.dto.ReviewResponse;
import com.trustshield.entity.AuditTrail;
import com.trustshield.entity.Review;
import com.trustshield.repository.AuditTrailRepository;
import com.trustshield.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReviewModerationService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuditTrailRepository auditTrailRepository;

    public ReviewResponse moderateReview(ReviewRequest request) {
        System.out.println("📝 REVIEW MODERATION STARTED");
        System.out.println("   Text: " + request.getReviewText());
        System.out.println("   Rating: " + request.getRating());
        
        // Calculate fraud score
        int fraudScore = calculateFraudScore(request);
        System.out.println("📊 Score: " + fraudScore);
        
        // Status
        String status = fraudScore > 70 ? "FLAG_FOR_REVIEW" : 
                       fraudScore > 50 ? "REVIEW" : "APPROVED";
        System.out.println("📊 Status: " + status);
        
        // Reasons
        List<String> reasons = new ArrayList<>();
        reasons.add("🤖 Analyzed by AI TrustShield");
        if (fraudScore > 70) {
            reasons.add("🔴 High probability of fake review");
        } else if (fraudScore > 50) {
            reasons.add("🟡 Suspicious patterns detected");
        } else {
            reasons.add("🟢 Review appears genuine");
        }
        
        // Check for exclamation marks
        long exclamationCount = request.getReviewText().chars().filter(ch -> ch == '!').count();
        if (exclamationCount > 3) {
            reasons.add("⚠️ Excessive exclamation marks (" + exclamationCount + " times)");
        }
        
        // Check for spammy keywords
        String text = request.getReviewText().toLowerCase();
        String[] spamKeywords = {"amazing", "best", "excellent", "great", "awesome"};
        int spamCount = 0;
        for (String keyword : spamKeywords) {
            if (text.contains(keyword)) spamCount++;
        }
        if (spamCount > 2) {
            reasons.add("⚠️ Contains " + spamCount + " spammy keywords");
        }
        
        // Generate review ID
        String reviewId = "REV" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Save
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setProductId(request.getProductId());
        review.setUserId(request.getUserId());
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());
        review.setFraudScore(fraudScore);
        review.setModerationStatus(status);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        System.out.println("💾 Review saved");
        
        // Audit
        AuditTrail audit = new AuditTrail();
        audit.setAuditId("AUD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        audit.setAgentName("review_agent");
        audit.setEntityId(reviewId);
        audit.setDecision(status);
        audit.setConfidenceScore(fraudScore);
        audit.setReasons(String.join("; ", reasons));
        audit.setCreatedAt(LocalDateTime.now());
        auditTrailRepository.save(audit);
        System.out.println("💾 Audit saved");
        
        // Response
        ReviewResponse response = new ReviewResponse();
        response.setFraudScore(fraudScore);
        response.setStatus(status);
        response.setReasons(reasons);
        response.setReviewId(reviewId);
        
        System.out.println("✅ REVIEW COMPLETE");
        return response;
    }

    private int calculateFraudScore(ReviewRequest request) {
        int score = 0;
        String text = request.getReviewText().toLowerCase();
        
        // 1. Exclamation marks
        long exclamationCount = request.getReviewText().chars().filter(ch -> ch == '!').count();
        if (exclamationCount > 3) score += 20;
        
        // 2. Spam keywords
        String[] spamKeywords = {"amazing", "best", "excellent", "great", "awesome"};
        int spamCount = 0;
        for (String keyword : spamKeywords) {
            if (text.contains(keyword)) spamCount++;
        }
        if (spamCount > 2) score += 15;
        
        // 3. Short review with 5 stars
        if (request.getReviewText().length() < 40 && request.getRating() == 5) {
            score += 15;
        }
        
        // 4. ALL CAPS
        long upperCount = request.getReviewText().chars().filter(Character::isUpperCase).count();
        if (request.getReviewText().length() > 0 && (double) upperCount / request.getReviewText().length() > 0.4) {
            score += 15;
        }
        
        // 5. Genuine indicators (reduce)
        if (text.contains("delivery") || text.contains("quality") || text.contains("price")) {
            score = Math.max(0, score - 10);
        }
        
        return Math.min(100, Math.max(0, score));
    }
}