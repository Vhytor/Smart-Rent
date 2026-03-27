package com.Vhytor.SmartRent.controllers;

import com.Vhytor.SmartRent.dtos.request.LoginRequest;
import com.Vhytor.SmartRent.dtos.request.RegisterRequest;
import com.Vhytor.SmartRent.dtos.response.RegisterResponse;
import com.Vhytor.SmartRent.enums.Role;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUserEmail(), request.getPassword());

        Map<String,String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // Login is usually handled by Spring Security internally
    // or via a Custom JWT Filter.
}
