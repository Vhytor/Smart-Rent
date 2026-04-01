package com.Vhytor.SmartRent.services;

import com.Vhytor.SmartRent.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


public interface PaymentService {

    Map<String, String> initializeTransaction(User user, BigDecimal amount, Long homeId);
    boolean verifyTransaction(String reference);
}
