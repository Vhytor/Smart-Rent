package com.Vhytor.SmartRent.controllers;

import com.Vhytor.SmartRent.dtos.LoginRequest;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authService.register(user);
        return ResponseEntity.ok("User registered successfully! You can now login.");
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUserEmail(), request.getPassword());

        Map<String,String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // Login is usually handled by Spring Security internally
    // or via a Custom JWT Filter.
}
