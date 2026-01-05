package com.gateway.controllers;

import com.gateway.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private MerchantRepository merchantRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test database connectivity by attempting a simple query
            merchantRepository.count(); // This will throw an exception if DB is not connected
            response.put("status", "healthy");
            response.put("database", "connected");
        } catch (Exception e) {
            response.put("status", "unhealthy");
            response.put("database", "disconnected");
        }
        
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        
        return ResponseEntity.ok(response);
    }
}