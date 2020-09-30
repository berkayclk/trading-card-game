package com.celik.domain.mana;

import com.celik.exception.HasNoCapacityException;
import com.celik.exception.InsufficientAmountException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Mana {

    public static final int MAX_SLOT_COUNT = 10;
    List<ManaSlot> manaSlots;

    private Mana() {
        manaSlots = new LinkedList<>();
    }

    public static Mana getEmptyMana() {
        return new Mana();
    }

    /**
     * Returns a Mana instance with mana slot count provided.
     * slotCount should be less than equal to max mana slot count.
     * @return ManBar
     */
    public static Mana getManaWithSlotCount(int slotCount) {
        return null;
    }

    public List<ManaSlot> getManaSlots() {
        return Collections.unmodifiableList(manaSlots);
    }

    /**
     * Adds a new empty slot to mana. If it has capacity
     * @throws HasNoCapacityException
     */
    public void addManaSlot() throws HasNoCapacityException {}

    public void refillManaSlots() {}

    public int getManaValue() { return 0; }

    public int getManaSlotCount() {
        return manaSlots.size();
    }

    public int getMaxSlotCount(){
        return MAX_SLOT_COUNT;
    }

    /**
     * uses mana as much as manaCost that is provided by parameter.
     * If there is no full mana slot as much as manaCost, throws an exception.
     * @param manaCost
     * @throws InsufficientAmountException
     */
    public void useMana(int manaCost) throws InsufficientAmountException { }

    public boolean hasAvailableManaFor(int manaCost) {
        return false;
    }

    private List<ManaSlot> getFullManaSlots(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mana mana = (Mana) o;
        return manaSlots.equals(mana.manaSlots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manaSlots);
    }

    @Override
    public String toString() {
        return String.format("Mana Bar: %d Mana / %d Capacity", getManaValue(), getManaSlotCount());
    }
}
