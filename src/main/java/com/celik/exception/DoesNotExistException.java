package com.celik.exception;

public class DoesNotExistException extends TradingCardException{
    public DoesNotExistException() {
    }

    public DoesNotExistException(String message) {
        super(message);
    }
}
