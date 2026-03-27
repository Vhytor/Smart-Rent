package com.Vhytor.SmartRent.exceptions;

/**
 * Base exception for all SmartRent application errors.
 * All custom exceptions extend this class.
 */
public class SmartRentException extends RuntimeException {

    public SmartRentException(String message) {
        super(message);
    }

    public SmartRentException(String message, Throwable cause) {
        super(message, cause);
    }
}
