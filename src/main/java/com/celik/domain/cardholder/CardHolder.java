package com.celik.domain.cardholder;

import com.celik.exception.DoesNotExistException;
import com.celik.exception.HasNoCapacityException;
import com.celik.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class CardHolder {

    static Logger logger = LoggerFactory.getLogger(CardHolder.class);

    protected Set<Card> cards;

    protected CardHolder(){
        cards = new HashSet<>();
    }

    public abstract boolean hasCapacity();

    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    public final Set<Card> getCards(){
        return Collections.unmodifiableSet(cards);
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public void reset(){
        cards.clear();
    }

    public int getCardCount(){
        return cards.size();
    }

    public void addCard(Card card) throws HasNoCapacityException {
        if( !hasCapacity() ) {
            throw new HasNoCapacityException("There is no capacity to add new card");
        }
        cards.add(card);
    }

    /**
     * Removes the card and return it.
     * @param card will be removed from this card holder.
     * @return removed card.
     * @throws DoesNotExistException -- if cards list doesnt contain card given at parameters
     */
    public Card giveCard(Card card) throws DoesNotExistException {

        if( !hasCard(card) ) {
            throw new DoesNotExistException("Card does not exists");
        }

        cards.remove( card );
        return card;
    }

    /**
     * Removes a card with the id of card.
     * @param cardId -- id of card that will be removed from this card holder.
     * @return removed card
     * @throws DoesNotExistException -- if card that have given cardId does not exists
     */
    public Card giveCard(int cardId) throws DoesNotExistException {
        Optional<Card> foundCard = findCardById(cardId);
        foundCard.ifPresent(card -> cards.remove(card) );
        return foundCard.orElseThrow(() -> new DoesNotExistException("Card does not exist"));
    }

    /**
     * Gives card to another card holder
     * @param card will be removed from this card holder.
     * @param another target card holder which will get caed
     * @throws DoesNotExistException -- if cards list doesnt contain card given at parameters
     * @throws HasNoCapacityException -- if target card holder has not capacity to add new card.
     */
    public void giveCard(Card card, CardHolder another) throws DoesNotExistException, HasNoCapacityException {

        if( !hasCard(card) ) {
            throw new DoesNotExistException("Card does not exists");
        }

        another.addCard(card);
        cards.remove( card );
    }

    public Optional<Card> findCardById(int cardId) {
        return cards.stream()
                .filter(card -> Integer.valueOf(cardId).equals(card.getId()))
                .findFirst();
    }
}
