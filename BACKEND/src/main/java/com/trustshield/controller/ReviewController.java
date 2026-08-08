package com.trustshield.controller;

import com.trustshield.dto.ReviewRequest;
import com.trustshield.dto.ReviewResponse;
import com.trustshield.service.ReviewModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewModerationService reviewModerationService;

    @PostMapping("/validate")
    public ResponseEntity<ReviewResponse> validateReview(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewModerationService.moderateReview(request);
        return ResponseEntity.ok(response);
    }
}