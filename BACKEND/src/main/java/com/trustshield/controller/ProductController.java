package com.trustshield.controller;

import com.trustshield.dto.ProductRequest;
import com.trustshield.dto.ProductResponse;
import com.trustshield.service.AuthenticityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private AuthenticityService authenticityService;

    @PostMapping("/validate")
    public ResponseEntity<ProductResponse> validateProduct(@RequestBody ProductRequest request) {
        ProductResponse response = authenticityService.checkAuthenticity(request);
        return ResponseEntity.ok(response);
    }
}