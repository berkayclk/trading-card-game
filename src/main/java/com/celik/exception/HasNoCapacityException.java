package com.celik.exception;

public class HasNoCapacityException extends TradingCardException {
    public HasNoCapacityException() {
    }

    public HasNoCapacityException(String message) {
        super(message);
    }
}
