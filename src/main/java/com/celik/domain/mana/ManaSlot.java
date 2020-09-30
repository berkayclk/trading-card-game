package com.celik.domain.mana;

import com.celik.exception.EmptyResourceUsingException;

import java.util.Objects;

public class ManaSlot {

    private ManaSlotState state;

    /**
     * returns empty manaSlot instance
     */
    public ManaSlot() {}

    public boolean isEmpty() {
        return false;
    }

    public boolean isFull() {
        return false;
    }

    public void fillManaSlot() {}

    public void useManaSlot() throws EmptyResourceUsingException {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManaSlot manaSlot = (ManaSlot) o;
        return state == manaSlot.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
