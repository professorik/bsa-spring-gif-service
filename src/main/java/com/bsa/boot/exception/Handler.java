package com.bsa.boot.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

// example of the exception handling in Spring
@ControllerAdvice
public final class Handler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleApplicationBusinessLogicException(ValidationException ex) {
        return ResponseEntity
            .unprocessableEntity()
            .body(
                Map.of(
                    "error", ex.getMessage() == null ? "Unprocessable request parameters" : ex.getMessage()
                )
            );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
