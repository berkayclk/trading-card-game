package com.celik.domain.cardholder;

import java.util.stream.Collectors;

public class Hand extends CardHolder {

    public static final int MAX_CARD_COUNT = 5;

    @Override
    public boolean hasCapacity() {
        return getCardCount() < MAX_CARD_COUNT;
    }

    @Override
    public String toString() {
        String cardDelimiter = "\n\t";

        String cardsInfo = "";
        if( !isEmpty() ) {
            cardsInfo = cardDelimiter + cards.stream().map(card -> card.toString())
                                                    .collect(Collectors.joining(cardDelimiter));
        }

        String tag = String.format("[HAS CAPACITY FOR %d CARD]", MAX_CARD_COUNT - getCardCount());
        if( isEmpty() ) tag = "[EMPTY]";
        else if( !hasCapacity() ) tag = "[FULL]";

        return String.format("Hand %s %s", tag, cardsInfo);
    }
}
