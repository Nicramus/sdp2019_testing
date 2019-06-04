package com.sdp.testing.bl;

/**
 * This exception is thrown when user try to do operation on two different currencies
 */
public class IncompatibleCurrencyUnitException extends Exception {

    public IncompatibleCurrencyUnitException(String message) {
        super(message);
    }
}
