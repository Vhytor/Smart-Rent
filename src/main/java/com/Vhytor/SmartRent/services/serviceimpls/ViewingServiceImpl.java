package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.exceptions.InvalidAccessCodeException;
import com.Vhytor.SmartRent.exceptions.PropertyNotFoundException;
import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import com.Vhytor.SmartRent.repositories.HomeRepository;
import com.Vhytor.SmartRent.repositories.ViewingRecordRepository;
import com.Vhytor.SmartRent.services.ViewingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ViewingServiceImpl implements ViewingService {

    private final HomeRepository homeRepository;
    private final ViewingRecordRepository viewingRecordRepository;

    // SecureRandom is thread-safe and cryptographically strong
    private static final SecureRandom secureRandom = new SecureRandom();


    public ViewingServiceImpl(HomeRepository homeRepository, ViewingRecordRepository viewingRecordRepository) {
        this.homeRepository = homeRepository;
        this.viewingRecordRepository = viewingRecordRepository;
    }

    @Override
    public Map<String, Object> processViewingRequest(Long homeId, User user) {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new PropertyNotFoundException(homeId));

        String accessCode = generateSecureCode();

        // Save the record in the database
        saveViewingRecord(user, home, accessCode);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment Successful");
        response.put("accessCode", accessCode);
        response.put("address", home.getAddress());

        return response;
    }

    @Override
    @Transactional //Ensures the update is saved to MYSQL correctly
    public boolean validateAccessCode(Long homeId, String code) {
        // 1. Find the latest record for this home and code
        // We'll need a custom method in ViewingRepository for this
        Optional<ViewingRecord> recordOpt = viewingRecordRepository.findByHomeHomeIdAndAccessCode(homeId, code)
                .stream()
                .filter(r -> !r.isUsed() && r.getExpiryTime().isAfter(LocalDateTime.now()))
                .findFirst();

        if (recordOpt.isEmpty()) {
            throw new InvalidAccessCodeException();

        }
        // 2. Mark as used so it can't be used again
            ViewingRecord record = recordOpt.get();
            record.setUsed(true);
            viewingRecordRepository.save(record);

            return true;
        }

//        return false;


    private void saveViewingRecord(User user, Home home, String accessCode) {
        ViewingRecord record = new ViewingRecord();

        // Set the relationships
        record.setUser(user);
        record.setHome(home);

        // Set the viewing data
        record.setAccessCode(accessCode);
        record.setIsPaid(true);

        // Set an expiration (e.g., the code is valid for 2 hours)
        record.setExpiryTime(LocalDateTime.now().plusHours(2));

        // Save to MySQL
        viewingRecordRepository.save(record);
    }

    private boolean simulatePayment(User user, BigDecimal amount) {
        System.out.println("Processing debit for: " + user.getUserEmail() + " Amount: " + amount);
        return true;
    }

    private String generateSecureCode() {
        int code = 100000 + ViewingServiceImpl.secureRandom.nextInt(900000); // 6-digit: 100000–999999
        return String.valueOf(code);
    }
}



