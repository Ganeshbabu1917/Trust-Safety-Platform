package com.trustshield.controller;

import com.trustshield.dto.RiskRequest;
import com.trustshield.dto.RiskResponse;
import com.trustshield.service.RiskScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:3000")
public class CheckoutController {

    @Autowired
    private RiskScoringService riskScoringService;

    @PostMapping("/validate")
    public ResponseEntity<RiskResponse> validateTransaction(@RequestBody RiskRequest request) {
        RiskResponse response = riskScoringService.analyzeTransaction(request);
        return ResponseEntity.ok(response);
    }
}