package com.celik.exception;

public class DeadPlayerException extends TradingCardException {
    public DeadPlayerException() {
    }

    public DeadPlayerException(String message) {
        super(message);
    }
}
