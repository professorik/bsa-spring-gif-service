package com.bsa.boot.exception;

public final class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
