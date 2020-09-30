package com.celik.exception;

public class EmptyResourceUsingException extends TradingCardException {
    public EmptyResourceUsingException() {
    }

    public EmptyResourceUsingException(String message) {
        super(message);
    }

    public EmptyResourceUsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
