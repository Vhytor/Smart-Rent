package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface PaymentService {
    
    String initializeTransaction(User user, BigDecimal amount, Long homeId);
    boolean verifyTransaction(String reference);
}
