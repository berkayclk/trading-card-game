package com.celik.model;

import com.celik.domain.damage.Damage;

import java.util.concurrent.atomic.AtomicInteger;

public class Card extends Damage implements Comparable<Card> {

    private static final AtomicInteger sequence = new AtomicInteger();

    private final int id;
    private final int manaCost;

    public Card(int manaCost) {
        super(manaCost);
        this.id = sequence.incrementAndGet();
        this.manaCost = manaCost;
    }

    public int getId() {
        return id;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id &&
                manaCost == card.manaCost;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Card -- ID: %d - Mana Cost: %d", id, manaCost);
    }

    @Override
    public int compareTo(Card o) {
        return o != null ? manaCost - o.getManaCost() : 1;
    }
}
