package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.dtos.request.RegisterRequest;
import com.Vhytor.SmartRent.dtos.response.RegisterResponse;
import com.Vhytor.SmartRent.enums.Role;
import com.Vhytor.SmartRent.exceptions.InvalidCredentialsException;
import com.Vhytor.SmartRent.exceptions.UserAlreadyExistsException;
import com.Vhytor.SmartRent.exceptions.UserNotFoundException;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.repositories.UserRepository;
import com.Vhytor.SmartRent.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor // Injects the repository automatically

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new TENANT.
     * Role is hardcoded here — the client has no influence over it.
     * Endpoint: POST /api/auth/register/tenant
     */
    public RegisterResponse registerTenant(RegisterRequest request) {
        return register(request, Role.TENANT);
    }

    /**
     * Registers a new LANDLORD.
     * Role is hardcoded here — the client has no influence over it.
     * Endpoint: POST /api/auth/register/landlord
     */
    public RegisterResponse registerLandlord(RegisterRequest request) {
        return register(request, Role.LANDLORD);
    }

    public RegisterResponse register(RegisterRequest registerRequest, Role role) {
        // Encode password before saving
        if (userRepository.findByUserEmail(registerRequest.getUserEmail()).isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.getUserEmail());
        }
        // Build the user entity — role is set here by the server
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setUserEmail(registerRequest.getUserEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(role); // <-- server decides this not the client

        User savedUser = userRepository.save(user);
        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getFullName(),
                savedUser.getUserEmail(),
                savedUser.getRole().name()
        );

    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    public String login(String userEmail, String password) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();

        }
        return jwtService.generateToken(user.getUserEmail());

    }
}
