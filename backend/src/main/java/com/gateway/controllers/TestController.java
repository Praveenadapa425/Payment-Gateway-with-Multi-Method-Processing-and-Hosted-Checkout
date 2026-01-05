package com.gateway.controllers;

import com.gateway.models.Merchant;
import com.gateway.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private MerchantRepository merchantRepository;

    @GetMapping("/api/v1/test/merchant")
    public ResponseEntity<?> getTestMerchant() {
        Optional<Merchant> merchantOpt = merchantRepository.findByEmail("test@example.com");
        
        if (merchantOpt.isPresent()) {
            Merchant merchant = merchantOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", merchant.getId().toString());
            response.put("email", merchant.getEmail());
            response.put("api_key", merchant.getApiKey());
            response.put("seeded", true);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}