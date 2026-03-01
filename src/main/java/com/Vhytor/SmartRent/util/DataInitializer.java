package com.Vhytor.SmartRent.util;

import com.Vhytor.SmartRent.model.Home;
import com.Vhytor.SmartRent.repositories.HomeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final HomeRepository homeRepository;

    public DataInitializer(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (homeRepository.count() == 0) {
            // House 1: Modern Apartment
            Home home1 = new Home();
            home1.setAddress("123 Silicon Valley Way, Lagos");
            home1.setDescription("Luxury 2-bedroom apartment with smart home features.");
            home1.setPricePerMonth(new BigDecimal("250000.00"));
            home1.setViewingFee(new BigDecimal("2000.00")); // The "little token"
            home1.setSmartLockCode("9999"); // Master code for testing

            // House 2: Cozy Studio
            Home home2 = new Home();
            home2.setAddress("45 Ocean View Drive, Lagos");
            home2.setDescription("Compact studio perfect for young professionals.");
            home2.setPricePerMonth(new BigDecimal("120000.00"));
            home2.setViewingFee(new BigDecimal("1000.00"));
            home2.setSmartLockCode("1234");

            homeRepository.save(home1);
            homeRepository.save(home2);

            System.out.println("SmartRent: Default homes have been added to MySQL.");
        }
    }
}