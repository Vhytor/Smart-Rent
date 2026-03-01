package com.Vhytor.SmartRent.controllers;

import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.repositories.UserRepository;
import com.Vhytor.SmartRent.services.ViewingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/homes")
public class HomeController {

    private final ViewingService viewingService;
    private final UserRepository userRepository;

    // Spring finds the implementation (ViewingServiceImpl) and injects it here
    public HomeController(ViewingService viewingService, UserRepository userRepository) {
        this.viewingService = viewingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{homeId}/request-access")
    public ResponseEntity<Map<String, Object>> requestAccess(@PathVariable Long homeId) {
        // 1. Get the currently logged-in user from the SecurityContext (via JWT)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User currentUser = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // 2. Process the request via the interface
        Map<String, Object> result = viewingService.processViewingRequest(homeId, currentUser);

        return ResponseEntity.ok(result);
    }
}
