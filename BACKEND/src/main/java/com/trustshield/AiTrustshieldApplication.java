package com.trustshield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiTrustshieldApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiTrustshieldApplication.class, args);
        System.out.println("🚀 AI TrustShield Backend Started!");
        System.out.println("📡 API Running on: http://localhost:8080");
    }
}