package com.Vhytor.SmartRent.services.serviceimpls;

import com.Vhytor.SmartRent.model.User;
import com.Vhytor.SmartRent.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${paystack.secret.key}")
    private String payStackSecretKeys;

    private final String PAYSTACK_INIT_URL = "https://api.paystack.co/transaction/initialize";

    @Override
    public String initializeTransaction(User user, BigDecimal amount, Long homeId) {
        RestTemplate restTemplate = new RestTemplate();

        // Paystack expects amount in Kobo (Naira * 100)
        BigDecimal amountInKobo = amount.multiply(new BigDecimal(100));

        Map<String, Object> body = new HashMap<>();
        body.put("email", user.getUserEmail());
        body.put("amount", amountInKobo.toString());
        body.put("callback_url", "http://localhost:8080/api/payments/verify");

        // Metadata helps us link the payment back to the specific house
        Map<String, String> metadata = new HashMap<>();
        metadata.put("home_id", homeId.toString());
        body.put("metadata", metadata);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payStackSecretKeys);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Send request to Paystack
        ResponseEntity<Map> response = restTemplate.postForEntity(PAYSTACK_INIT_URL, entity, Map.class);

        // Extract the authorization_url to send to the user
        Map data = (Map) response.getBody().get("data");
        return data.get("authorization_url").toString();
    }

    @Override
    public boolean verifyTransaction(String reference) {
        // Implementation for Paystack verification endpoint
        // https://api.paystack.co/transaction/verify/:reference
        return true; // Placeholder for now
    }
}
