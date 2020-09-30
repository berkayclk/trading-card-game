package com.celik.domain.mana;

import com.celik.exception.EmptyResourceUsingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManaSlotTest {

    @Test
    public void whenCreateManaSlot_shouldBeEmpty() {
        ManaSlot newSlot = new ManaSlot();
        Assertions.assertTrue(newSlot.isEmpty());
    }

    @Test
    public void whenInvokeFill_manaSlotShouldBeFull() {
        //arrange
        ManaSlot newSlot = new ManaSlot();

        //act
        newSlot.fillManaSlot();

        //assert
        Assertions.assertTrue(newSlot.isFull());
        Assertions.assertFalse(newSlot.isEmpty());
    }

    @Test
    public void whenUsedFullMana_manaShouldBeEmpty() {
        //arrange
        ManaSlot newSlot = new ManaSlot();
        newSlot.fillManaSlot();

        //act
        Assertions.assertDoesNotThrow(newSlot::useManaSlot, "When there is at least a full slot, useManaSlot should not throw an error");

        //assert
        Assertions.assertTrue(newSlot.isEmpty());
        Assertions.assertFalse(newSlot.isFull());
    }

    @Test
    public void whenUsedEmptyMana_manaShouldThrowEmptyManaException() {
        //arrange
        ManaSlot newSlot = new ManaSlot();

        //act - assert
        Assertions.assertThrows(EmptyResourceUsingException.class, newSlot::useManaSlot);
    }
}
