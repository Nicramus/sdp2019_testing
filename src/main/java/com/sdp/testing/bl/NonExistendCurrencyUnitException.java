package com.sdp.testing.bl;

/**
 * Exception class - this is throwing when user try to use non existend currency.
 */
public class NonExistendCurrencyUnitException extends RuntimeException {
    public NonExistendCurrencyUnitException(String message) {
        super(message);
    }
}
