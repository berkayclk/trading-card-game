package com.celik.domain.mana;

import com.celik.exception.EmptyResourceUsingException;
import com.celik.exception.HasNoCapacityException;
import com.celik.exception.InsufficientAmountException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mana {

    static private Logger logger = LoggerFactory.getLogger(Mana.class);

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
        Mana mana = new Mana();
        IntStream.range(0, slotCount).forEach(i -> {
            try {
                mana.addManaSlot();
            } catch (HasNoCapacityException e) {
                logger.warn("Tried to add mana slot when mana has not capacity.");
            }
        });
        return mana;
    }

    public List<ManaSlot> getManaSlots() {
        return Collections.unmodifiableList(manaSlots);
    }

    /**
     * Adds a new empty slot to mana. If it has capacity
     * @throws HasNoCapacityException
     */
    public void addManaSlot() throws HasNoCapacityException {
        if ( !canIncreaseManaSlot() ) {
            throw new HasNoCapacityException("There is no capacity to add new mana slot.");
        }
        manaSlots.add(new ManaSlot());
        logger.info("Added a mana slot. New mana slot capacity: {}", getManaSlotCount());
    }

    public void refillManaSlots() {
        manaSlots.stream().forEach(manaSlot -> manaSlot.fillManaSlot());
        logger.info("Filled mana slots. New mana value: {}", getManaValue() );
    }

    public int getManaValue() {
        if (manaSlots.isEmpty()) return 0;
        return getFullManaSlots().size();
    }

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
    public void useMana(int manaCost) throws InsufficientAmountException {
        logger.info("Requested to use {} mana", manaCost);
        if ( !hasAvailableManaFor(manaCost) ) {
            throw new InsufficientAmountException("There is no mana to using as manaCost");
        };

        int usedMana = 0;
        List<ManaSlot> fullSlots = getFullManaSlots();
        for(ManaSlot slot : fullSlots){
            if(usedMana == manaCost ) break;
            try {
                slot.useManaSlot();
                usedMana++;
            } catch (EmptyResourceUsingException e) {
                logger.error("Tried to use empty mana slot while using mana cost.");
            }
        }

        logger.info("Used {} mana. Remaining mana value is {}", usedMana, getManaValue());
    }

    public boolean hasAvailableManaFor(int manaCost) {
        if( manaSlots.size() == 0 ) return false;
        return getManaValue() >= manaCost;
    }

    private boolean canIncreaseManaSlot() {
        return manaSlots.size() < getMaxSlotCount();
    }

    private List<ManaSlot> getFullManaSlots(){
        return manaSlots.stream()
                .filter(manaSlot -> manaSlot.isFull())
                .collect(Collectors.toList());
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
