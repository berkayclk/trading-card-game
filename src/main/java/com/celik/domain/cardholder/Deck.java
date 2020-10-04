package com.celik.domain.cardholder;

import com.celik.exception.EmptyResourceUsingException;
import com.celik.model.Card;

import java.util.List;
import java.util.Random;

public class Deck extends CardHolder {

    private static final Random random = new Random();

    public static Deck getDeckWithManaCosts(List<Integer> manaCosts) {
        Deck deck = new Deck();
        manaCosts.stream().map(Card::new).forEach(deck.cards::add);
        return deck;
    }

    @Override
    public boolean hasCapacity() {
        return true; // there is no restriction for deck, for now.
    }

    /**
     * Picks a random card from deck.
     * @return a random card
     * @throws EmptyResourceUsingException
     */
    public Card pickRandomCard() throws EmptyResourceUsingException {
        if( getCardCount() <= 0 ) {
            throw new EmptyResourceUsingException("There is no card to pick");
        }

        int randomCardIndex = random.nextInt(cards.size());
        Card pickedCard = cards.stream().skip(randomCardIndex).findFirst().get();

        return pickedCard;
    }

    @Override
    public String toString() {
        return String.format("Deck Size: %d", getCardCount());
    }
}
