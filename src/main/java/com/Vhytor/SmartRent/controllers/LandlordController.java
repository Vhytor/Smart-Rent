package com.Vhytor.SmartRent.controllers;

import com.Vhytor.SmartRent.dtos.request.CreateHomeRequest;
import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import com.Vhytor.SmartRent.repositories.UserRepository;
import com.Vhytor.SmartRent.services.LandlordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlord")
public class LandlordController {

    private final LandlordService landlordService;
    private final UserRepository userRepository;


    public LandlordController(LandlordService landlordService, UserRepository userRepository) {
        this.landlordService = landlordService;
        this.userRepository = userRepository;
    }

    @GetMapping("/properties")
    public ResponseEntity<List<Home>> getMyProperties() {
        User landlord = getCurrentUser();
        return ResponseEntity.ok(landlordService.getMyProperties(landlord));
    }

    @GetMapping("/properties/{homeId}/history")
    public ResponseEntity<List<ViewingRecord>> getViewingHistory(@PathVariable Long homeId) {
        User landlord = getCurrentUser();
        return ResponseEntity.ok(landlordService.getMyViewingRecords(homeId, landlord));
    }
    @PostMapping("/properties")
    public ResponseEntity<Home> createProperty(@Valid @RequestBody CreateHomeRequest request) {
        User landlord = getCurrentUser();
        return ResponseEntity.ok(landlordService.createProperty(request, landlord));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}