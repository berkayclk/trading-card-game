package com.celik.domain.mana;

import com.celik.exception.EmptyResourceUsingException;

import java.util.Objects;

public class ManaSlot {

    private ManaSlotState state;

    /**
     * returns empty manaSlot instance
     */
    public ManaSlot() {
        this.state = ManaSlotState.EMPTY;
    }

    public boolean isEmpty() {
        return ManaSlotState.EMPTY.equals(state);
    }

    public boolean isFull() {
        return ManaSlotState.FULL.equals(state);
    }

    public void fillManaSlot() {
        this.state = ManaSlotState.FULL;
    }

    public void useManaSlot() throws EmptyResourceUsingException {
        if( !isFull() ) {
            throw new EmptyResourceUsingException();
        }
        this.state = ManaSlotState.EMPTY;
    }

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
