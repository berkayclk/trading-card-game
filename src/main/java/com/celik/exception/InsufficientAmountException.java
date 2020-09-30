package com.celik.exception;

public class InsufficientAmountException extends TradingCardException {
    public InsufficientAmountException() {
    }

    public InsufficientAmountException(String message) {
        super(message);
    }
}
