package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.dtos.request.RegisterRequest;
import com.Vhytor.SmartRent.dtos.response.LoginResponseDTO;
import com.Vhytor.SmartRent.dtos.response.RegisterResponse;
import com.Vhytor.SmartRent.enums.Role;
import com.Vhytor.SmartRent.exceptions.EmailNotVerifiedException;
import com.Vhytor.SmartRent.exceptions.InvalidCredentialsException;
import com.Vhytor.SmartRent.exceptions.UserAlreadyExistsException;
import com.Vhytor.SmartRent.exceptions.UserNotFoundException;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.repositories.UserRepository;
import com.Vhytor.SmartRent.util.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
//@RequiredArgsConstructor // Injects the repository automatically

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
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

        String verificationCode = generateVerificationCode();

        // Build the user entity — role is set here by the server
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setUserEmail(registerRequest.getUserEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(role); // <-- server decides this not the client
        user.setVerified(false);
        user.setVerificationCode(verificationCode);

        User savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(
                savedUser.getUserEmail(),
                savedUser.getFullName(),
                verificationCode
        );

        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getFullName(),
                savedUser.getUserEmail(),
                savedUser.getRole().name()
        );

    }

    public void verifyEmail(String email, String code) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (user.isVerified()) {
            return; // Already verified — idempotent, no error needed
        }

        if (!code.equals(user.getVerificationCode())) {
            throw new InvalidCredentialsException();
        }

        user.setVerified(true);
        user.setVerificationCode(null); // Clear code — single use only
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (user.isVerified()) {
            return; // Nothing to resend
        }

        String newCode = generateVerificationCode();
        user.setVerificationCode(newCode);
        userRepository.save(user);

        emailService.sendVerificationEmail(
                user.getUserEmail(),
                user.getFullName(),
                newCode
        );
    }

    /**
     * Authenticates a user and returns a JWT token.
     */
    public LoginResponseDTO login(String userEmail, String password) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();

        }

        if (!user.isVerified()) {
            throw new EmailNotVerifiedException(userEmail);
        }

        String token = jwtService.generateToken(user.getUserEmail());

        return new LoginResponseDTO(
                token,
                user.getUserId(),
                user.getFullName(),
                user.getUserEmail(),
                user.getRole()
        );

    }

    private String generateVerificationCode() {
        int code = 100000 + secureRandom.nextInt(900000); // 6-digit
        return String.valueOf(code);
    }
}
