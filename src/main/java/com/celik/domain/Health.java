package com.celik.domain;

import com.celik.exception.InsufficientAmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Health {

    static Logger logger = LoggerFactory.getLogger(Health.class);

    int value;

    public Health(){
        logger.info("Health: {}", value);
    }

    public Health(int value) {
        this.value = value >= 0 ? value : 0;
    }

    public void increaseHealth() {
        value++;
        logger.info("Health was increased. Health: {}", value);
    }

    public void increaseHealth(int increaseValue) throws IllegalArgumentException {
        if( increaseValue <= 0 ) {
            logger.error("Tried to increase health with negative number. Health: {}", value);
            throw new IllegalArgumentException("increaseValue should be positive");
        }
        value += increaseValue;
        logger.info("Health was increased with {}. Health: {}", increaseValue, value);
    }

    public void decreaseHealth() throws InsufficientAmountException {
        if( value == 0 ) {
            logger.info("Tried to decrease health that has no value. Health: {}", value);
            throw new InsufficientAmountException("There is no available health to decrease");
        }
        --value;
        logger.info("Health was decreased. Health: {}", value);
    }

    public void decreaseHealth(int decreaseValue) throws InsufficientAmountException {
        if( value == 0 || decreaseValue > value) {
            value = 0;
            logger.info("Tried to decrease health with value over from health. Health: {}", value);
            throw new InsufficientAmountException("There is no available health to decrease");
        } else if( decreaseValue < 0 ) {
            logger.error("Tried to decrease health with negative number. Health: {}", value);
            throw new IllegalArgumentException("increaseValue should be positive");
        }

        value -= decreaseValue;
        logger.info("Health was decreased with {}. Health: {}", decreaseValue, value);

    }

    public int getHealthValue() {
        return this.value;
    }

    public boolean hasHealth() {
        return value > 0;
    }

    @Override
    public String toString() {
        return String.format("Health: %d", value);
    }
}
