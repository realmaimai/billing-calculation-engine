package com.maimai.billingcalculationengine.common.handler;

import com.maimai.billingcalculationengine.common.exception.AccountNotFoundException;
import com.maimai.billingcalculationengine.common.exception.PasswordMismatchException;
import com.maimai.billingcalculationengine.common.exception.UnauthorizedException;
import com.maimai.billingcalculationengine.common.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // universal error handler
    @ExceptionHandler(value = Exception.class)
    public Result<Object> handleException(Exception e) {
        log.error("unknown error happened: {}", e.getMessage());

        return Result.fail(e.getMessage());
    }

    @ExceptionHandler({AccountNotFoundException.class, PasswordMismatchException.class, UnauthorizedException.class})
    public Result<Object> handleAuthenticationExceptions(Exception ex) {
        log.error("authentication error: {}", ex.getMessage());

        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        log.error("Validation error: {}", errorMessage);
        return Result.fail(400, errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validation failed");

        log.error("Constraint violation: {}", errorMessage);
        return Result.fail(400, errorMessage);
    }
}
