package com.mealmap.mealmap_backend_api.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
