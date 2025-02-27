package com.maimai.billingcalculationengine.common.exception;

/**
 * General base exception: other exceptions will inherit this class
 */
public class BaseException extends RuntimeException {
    private String code;

    public BaseException() {}

    public BaseException(String message) {
        super(message);
        this.code = "500";
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }
}
