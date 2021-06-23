package com.bsa.boot.exception;

/**
 * @author professorik
 * @created 22/06/2021 - 09:41
 * @project boot
 */
public final class NoHeaderException extends RuntimeException {
    public NoHeaderException(String message) {
        super(message);
    }
}