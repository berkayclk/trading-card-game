package com.celik.domain.mana;

import com.celik.exception.HasNoCapacityException;
import com.celik.exception.InsufficientAmountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManaTest {

    @Test
    public void whenGetEmptyMana_hasNotManaSlot(){
        //arrange
        Mana mana = Mana.getEmptyMana();

        //act
        int manaSlotCount = mana.getManaSlotCount();

        //assert
        Assertions.assertEquals(0, manaSlotCount, "Default mana slot count should be 0");
    }

    @Test
    public void maxManSlotShouldBeTen(){
        // arrange
        Mana mana = Mana.getEmptyMana();

        //assert
        Assertions.assertEquals(10, mana.getMaxSlotCount());
    }

    @Test
    public void whenSlotCountLessThanMax_canAddNewSlot(){
        //arrange
        Mana mana = Mana.getEmptyMana();

        int lastManaSlotCount = mana.getManaSlotCount();
        while ( lastManaSlotCount < mana.getMaxSlotCount() ) {
            Assertions.assertDoesNotThrow(mana::addManaSlot);
            Assertions.assertEquals( lastManaSlotCount + 1, mana.getManaSlotCount());
            lastManaSlotCount = mana.getManaSlotCount();
        }
    }

    @Test
    public void whenSlotCountIsMax_canNotAddNewSlot(){
        //arrange
        Mana mana = Mana.getEmptyMana();

        int lastSlotCount = mana.getManaSlotCount();
        while ( lastSlotCount < mana.getMaxSlotCount() ) {
            Assertions.assertDoesNotThrow(mana::addManaSlot);
            Assertions.assertEquals( lastSlotCount + 1, mana.getManaSlotCount());
            lastSlotCount = mana.getManaSlotCount();
        }

        //act-assert
        int manaSlotCountBeforeAdding = mana.getManaSlotCount();
        Assertions.assertThrows(HasNoCapacityException.class, mana::addManaSlot);
        Assertions.assertEquals(manaSlotCountBeforeAdding, mana.getManaSlotCount());
    }

    @Test
    public void whenCreateManaWithSlotCount_shouldInitWithCountSlot(){

        //arrange
        int manaSlotCount = Mana.MAX_SLOT_COUNT - 1;
        Mana mana = Mana.getManaWithSlotCount(manaSlotCount);
        Assertions.assertNotNull(mana);

        //assert
        Assertions.assertEquals(manaSlotCount, mana.getManaSlotCount());
    }


    @Test
    public void whenCreateManaWithSlotCountGreaterThanMaxCount_shouldSetOnlyMaxCountSlot(){

        //arrange
        int overMaxManaSlotCount = Mana.MAX_SLOT_COUNT + 1;
        Mana mana = Mana.getManaWithSlotCount(overMaxManaSlotCount);
        Assertions.assertNotNull(mana);

        //assert
        Assertions.assertEquals(Mana.MAX_SLOT_COUNT, mana.getManaSlotCount());
    }

    @Test
    public void getManaValue_shouldReturnFullManaSlotCount() {

        //arrange
        Mana mana = Mana.getEmptyMana();
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        Assertions.assertDoesNotThrow(mana::addManaSlot);

        Assertions.assertEquals(3, mana.getManaSlotCount());

        mana.refillManaSlots();
        Assertions.assertEquals(3, mana.getManaValue());

        Assertions.assertDoesNotThrow( () -> mana.useMana(2), "Exception should not be thrown when using available mana");

        //assert
        Assertions.assertEquals(3, mana.getManaSlotCount());
        Assertions.assertEquals(1, mana.getManaValue());
    }

    @Test
    public void whenRefillManaSlot_allEmptySlotsShouldBeFull() {

        //arrange
        Mana mana = Mana.getEmptyMana();
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        Assertions.assertDoesNotThrow(mana::addManaSlot);

        Assertions.assertEquals(3, mana.getManaSlotCount());
        Assertions.assertEquals(0, mana.getManaValue());

        //act
        mana.refillManaSlots();

        //assert
        Assertions.assertEquals(3, mana.getManaSlotCount());
        Assertions.assertEquals(3, mana.getManaValue());
    }

    @Test
    public void whenUsingMana_manaValueShouldDecrease(){

        //arrange
        Mana mana = Mana.getEmptyMana();
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        mana.refillManaSlots();

        //act
        Assertions.assertDoesNotThrow(() -> mana.useMana(1), "Exception should not be thrown when using available mana");

        //assert
        Assertions.assertEquals(0, mana.getManaValue());
    }

    @Test
    public void whenUsingManaOverCapacity_shouldInsufficientAmountExceptionThrown(){

        //arrange
        Mana mana = Mana.getEmptyMana();

        //act-assert
        Assertions.assertThrows(InsufficientAmountException.class, () -> mana.useMana(1));
    }

    @Test
    public void whenThereIsAvailableMana_hasAvailableManaFor_shouldReturnTrue() {

        //arrange
        Mana mana = Mana.getEmptyMana();
        Assertions.assertDoesNotThrow(mana::addManaSlot);
        mana.refillManaSlots();

       //act
        boolean hasAvailableManaFor = mana.hasAvailableManaFor(1);

        //arrange
        Assertions.assertTrue(hasAvailableManaFor);
    }

    @Test
    public void whenThereIsNotAvailableMana_hasAvailableManaFor_shouldReturnFalse(){

        //arrange
        Mana mana = Mana.getEmptyMana();

        //act
        boolean hasAvailableManaFor = mana.hasAvailableManaFor(1);

        //arrange
        Assertions.assertFalse(hasAvailableManaFor);
    }
}
