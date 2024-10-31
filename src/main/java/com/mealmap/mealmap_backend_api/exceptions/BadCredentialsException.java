package com.mealmap.mealmap_backend_api.exceptions;

public class BadCredentialsException extends RuntimeException{

    public BadCredentialsException() {
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
