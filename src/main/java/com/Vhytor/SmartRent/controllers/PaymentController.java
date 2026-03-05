package com.Vhytor.SmartRent.controllers;

import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.repositories.UserRepository;
import com.Vhytor.SmartRent.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public PaymentController(PaymentService paymentService, UserRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/initialize/{homeId}")
    public ResponseEntity<Map<String, String>> initialize(@PathVariable Long homeId, @RequestParam BigDecimal amount) {
        // 1. Identify the Tenant
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Call the Service to get the Paystack URL
        String paymentUrl = paymentService.initializeTransaction(user, amount, homeId);

        // 3. Return the URL so the frontend can redirect the user
        return ResponseEntity.ok(Map.of("checkoutUrl", paymentUrl));
    }
}
