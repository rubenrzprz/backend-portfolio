package com.ruben.backendportfolio.errors;

import com.ruben.backendportfolio.items.domain.ItemNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<Map<String, String>> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();
        ApiError body = ApiError.of("validation_error", "Validation failed", details, req.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        ApiError body = ApiError.of("validation_error", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({ItemNotFoundException.class, NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        ApiError body = ApiError.of("not_found", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        ApiError body = ApiError.of("internal_error", "Unexpected error", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, String> toDetail(FieldError fe) {
        return Map.of("field", fe.getField(), "message", fe.getDefaultMessage());
    }
}
