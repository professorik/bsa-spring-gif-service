package com.bsa.boot.exception;

public final class EntityNotFoundException extends RuntimeException {
    private static final String DEFAULT_MSG = "Resource not found";

    public EntityNotFoundException() {
        super(DEFAULT_MSG);
    }
}
