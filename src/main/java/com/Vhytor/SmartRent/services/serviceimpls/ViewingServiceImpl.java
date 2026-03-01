package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.model.ViewingRecord;
import com.Vhytor.SmartRent.repositories.HomeRepository;
import com.Vhytor.SmartRent.repositories.ViewingRecordRepository;
import com.Vhytor.SmartRent.services.ViewingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ViewingServiceImpl implements ViewingService {

    private final HomeRepository homeRepository;
    private final ViewingRecordRepository viewingRecordRepository;

    @Autowired
    public ViewingServiceImpl(HomeRepository homeRepository, ViewingRecordRepository viewingRecordRepository) {
        this.homeRepository = homeRepository;
        this.viewingRecordRepository = viewingRecordRepository;
    }

    @Override
    public Map<String, Object> processViewingRequest(Long homeId, User user) {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!simulatePayment(user, home.getViewingFee())) {
            throw new RuntimeException("Payment failed");
        }

        String accessCode = generateSecureCode();

        // Save the record in the database
        saveViewingRecord(user, home, accessCode);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment Successful");
        response.put("accessCode", accessCode);
        response.put("address", home.getAddress());

        return response;
    }

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
        return String.valueOf((int)((Math.random() * 8999) + 1000)); // 4-digit code
    }
}
