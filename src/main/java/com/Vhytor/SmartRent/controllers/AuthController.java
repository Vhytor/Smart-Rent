package com.Vhytor.SmartRent.controllers;


import com.Vhytor.SmartRent.dtos.request.RegisterRequest;
import com.Vhytor.SmartRent.dtos.response.LoginResponseDTO;
import com.Vhytor.SmartRent.dtos.response.RegisterResponse;

import com.Vhytor.SmartRent.services.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register/tenant
     * Registers a new tenant. Role is assigned server-side as TENANT.
     */
    @PostMapping("/register/tenant")
    public ResponseEntity<RegisterResponse> registerTenant(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.registerTenant(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    /**
     * POST /api/auth/register/landlord
     * Registers a new landlord. Role is assigned server-side as LANDLORD.
     */
    @PostMapping("/register/landlord")
    public ResponseEntity<RegisterResponse> registerLandlord(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = authService.registerLandlord(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody Map<String, String> credentials) {

        LoginResponseDTO loginResponseDTO = authService.login(
                credentials.get("userEmail"),
                credentials.get("password")

        );
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);


        // Login is usually handled by Spring Security internally
        // or via a Custom JWT Filter.
    }

    /**
     * POST /api/auth/verify
     * Verifies a user's email using the 6-digit code sent at registration.
     *
     * Request body: { "email": "...", "code": "123456" }
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(
            @RequestBody Map<String, String> body) {
        authService.verifyEmail(body.get("email"), body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Email verified successfully. You can now log in."));
    }

    /**
     * POST /api/auth/resend-code
     * Resends a fresh verification code to the user's email.
     *
     * Request body: { "email": "..." }
     */
    @PostMapping("/resend-code")
    public ResponseEntity<Map<String, String>> resendCode(
            @RequestBody Map<String, String> body) {
        authService.resendVerificationCode(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "A new verification code has been sent to your email."));
    }

}
