package com.celik.domain;

import com.celik.exception.InsufficientAmountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HealthTest {

    @Test
    public void whenGetNewInstance_healthShouldBeZero() {
        //arrange
        Health health = new Health();

        //assert
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());
    }

    // hasHealth
    @Test
    public void whenHealthIsGreaterThanZero_hasHealthShouldBeTrue() {
        //arrange
        Health health = new Health(1);
        Assertions.assertEquals(1, health.getHealthValue());

        //assert
        Assertions.assertTrue(health.hasHealth());

    }

    @Test
    public void whenHealthIsZero_hasHealthShouldBeFalse() {
        //arrange
        Health health = new Health();
        Assertions.assertEquals(0, health.getHealthValue());

        //assert
        Assertions.assertFalse(health.hasHealth());
    }

    // decreaseHealth

    @Test
    public void whenHealthIsZero_decreaseHealth_shouldThrowException() {
        //arrange
        Health health = new Health();
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

        //assert
        Assertions.assertThrows(InsufficientAmountException.class, health::decreaseHealth);

    }

    @Test
    public void whenHealthIsZero_decreaseHealthWithValue_shouldThrowException() {
        //arrange
        Health health = new Health();
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

        //assert
        Assertions.assertThrows(InsufficientAmountException.class, () -> health.decreaseHealth(5));

    }

    @Test
    public void whenHealthIsGreaterThanZero_decreaseHealth_shouldDecreaseHealth() {
        //arrange
        Health health = new Health(10);
        Assertions.assertEquals(10, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertDoesNotThrow(() -> health.decreaseHealth());
        Assertions.assertEquals(9, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());
    }

    @Test
    public void whenHealthIsGreaterThanZero_decreaseHealthWithValue_shouldDecreaseHealth() {
        //arrange
        Health health = new Health(10);
        Assertions.assertEquals(10, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertDoesNotThrow(() -> health.decreaseHealth(5));
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

    }

    @Test
    public void whenHealthIsDecreasedAsMuchAsValue_hasHealthShouldReturnFalse() {
        //arrange
        Health health = new Health(10);
        Assertions.assertEquals(10, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertDoesNotThrow(() -> health.decreaseHealth(10));
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

    }

    @Test
    public void whenHealthIsDecreasedOverFromValue_shouldThrownInsufficientAmountException() {
        //arrange
        Health health = new Health(5);
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertThrows(InsufficientAmountException.class, () -> health.decreaseHealth(10));

    }

    @Test
    public void whenHealthIsDecreasedOverFromValue_healthShouldBeZero() {
        //arrange
        Health health = new Health(5);
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertThrows(InsufficientAmountException.class, () -> health.decreaseHealth(10));
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

    }

    @Test
    public void whenHealthIsDecreased_withNegativeValue_shouldThrowsIllegalArgumentException() {
        //arrange
        Health health = new Health(5);
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> health.decreaseHealth(-10));
        Assertions.assertEquals(5, health.getHealthValue()); //not to be changed
        Assertions.assertTrue(health.hasHealth());

    }

    @Test
    public void whenHealthIsIncreased_withNegativeValue_shouldThrowsIllegalArgumentException() {
        //arrange
        Health health = new Health(5);
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());

        //assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> health.increaseHealth(-10));
        Assertions.assertEquals(5, health.getHealthValue()); //not to be changed
        Assertions.assertTrue(health.hasHealth());

    }

    @Test
    public void whenHealthIsIncreased_withValidValue_shouldIncreaseHealth() {
        //arrange
        Health health = new Health();
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

        //assert
        Assertions.assertDoesNotThrow(() -> health.increaseHealth(5));
        Assertions.assertEquals(5, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());
    }

    @Test
    public void whenHealthIsIncreased_shouldIncreaseHealth() {
        //arrange
        Health health = new Health();
        Assertions.assertEquals(0, health.getHealthValue());
        Assertions.assertFalse(health.hasHealth());

        //assert
        Assertions.assertDoesNotThrow(() -> health.increaseHealth());
        Assertions.assertEquals(1, health.getHealthValue());
        Assertions.assertTrue(health.hasHealth());
    }
}
