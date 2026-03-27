package com.Vhytor.SmartRent.exceptions;

/**
 * Thrown when a Paystack transaction reference fails verification.
 * Maps to HTTP 402 Payment Required.
 */
public class PaymentVerificationException extends SmartRentException {

    public PaymentVerificationException(String reference) {
        super("Payment verification failed for transaction reference: " + reference);
    }
}
