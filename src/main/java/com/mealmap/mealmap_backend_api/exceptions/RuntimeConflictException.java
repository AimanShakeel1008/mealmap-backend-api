package com.mealmap.mealmap_backend_api.exceptions;

public class RuntimeConflictException extends RuntimeException{

    public RuntimeConflictException() {
    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
