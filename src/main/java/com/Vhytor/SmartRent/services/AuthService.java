package com.Vhytor.SmartRent.services;

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

    public User register(User user) {
        // Encode password before saving
        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUserEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String userEmail, String password) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();

        }
        return jwtService.generateToken(user.getUserEmail());

    }
}
