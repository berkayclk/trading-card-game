package com.celik.exception;

public class TradingCardException extends Exception {
    public TradingCardException() {
    }

    public TradingCardException(String message) {
        super(message);
    }

    public TradingCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
