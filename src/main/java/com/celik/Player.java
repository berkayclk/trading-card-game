package com.celik;

import com.celik.domain.Health;
import com.celik.domain.cardholder.Deck;
import com.celik.domain.cardholder.Hand;
import com.celik.domain.damage.DamageEvent;
import com.celik.domain.damage.DamageEventListener;
import com.celik.domain.damage.Damager;
import com.celik.domain.mana.Mana;
import com.celik.exception.*;
import com.celik.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Damager implements DamageEventListener {

    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    private static final AtomicInteger sequence = new AtomicInteger();

    private final int id;
    private final String name;
    private final Health health;
    private final Mana mana;
    private final Deck deck;
    private final Hand hand;

    public Player(String name, Health health, Mana mana, Deck deck) {
        super();
        this.id = sequence.incrementAndGet();
        logger.info("Player {} is initializing...", id);

        this.name = name;
        this.health = health;
        this.mana = mana;
        this.deck = deck;
        this.hand = new Hand();
    }

    /**
     * Draws a random card from deck.
     * @throws EmptyResourceUsingException -- when deck has not card
     * @throws HasNoCapacityException -- When hand exceeded the max capacity
     */
    public void drawCard() throws EmptyResourceUsingException, HasNoCapacityException {
        logger.info("Player {} is drawing a card.", id);
        Card card = getDeck().pickRandomCard();

        try {
            getDeck().giveCard(card, getHand());
        } catch (DoesNotExistException e) {
            logger.error("Picked card could not found in the Deck");
            throw new EmptyResourceUsingException("Card could not found in the Deck");
        }
    }

    /**
     * Player plays an existing card and inflict damage to opponent players.
     * @param cardId -- id of the card that will be played
     * @throws DoesNotExistException -- when there is no card with cardId in the hand
     * @throws InsufficientAmountException -- when there is no mana to play the card.
     * @throws DeadPlayerException -- when player was dead
     */
    public void playCard(int cardId) throws DoesNotExistException, InsufficientAmountException, DeadPlayerException {
        if( isDead() ) {
            throw new DeadPlayerException("Player is dead");
        }

        Optional<Card> card = getHand().findCardById(cardId);
        if( card.isEmpty() ) {
            logger.error("Player {} has not any card with {} id.", id, cardId);
            throw new DoesNotExistException("Card does not exists in the hand");
        }

        if( card.get().getManaCost() > 0 ) {
            getMana().useMana(card.get().getManaCost());
        }

        logger.info("Player {} is playing {}", id, card.get());

        Card playCard = getHand().giveCard(card.get());
        if( playCard.getDamageAmount() > 0 ) {
            inflictDamage(playCard);
        }
    }

    public boolean canPlayCard(){
        return getHand().getCardCount() > 0 && getMinManaCostCard().isPresent() && getMinManaCostCard().get().getManaCost() <= getMana().getManaValue();
    }

    private Optional<Card> getMinManaCostCard(){
        return getHand().getCards().stream().min(Card::compareTo);
    }

    public void takeDamage(int damageAmount) {
        if( !getHealth().hasHealth() ) {
            logger.info("Player {} has not health to take damage!", id);
            return;
        }

        logger.info("Player {} is taking {} damage", id, damageAmount);
        try {
            getHealth().decreaseHealth(damageAmount);
        } catch (InsufficientAmountException e) {
            logger.warn("Player {} was dead!", id);
            clearDamageListener();
        }
    }

    @Override
    public void takeDamage(DamageEvent damageEvent) {
        if( damageEvent.isDamageSource(this)) {
            logger.warn("Self damaging was prevented.");
            return;
        }

        takeDamage(damageEvent.getDamage().getDamageAmount());
    }

    public void addOpponentPlayer(Player opponentPlayer) {
        addDamageListener(opponentPlayer);
    }

    public int getOpponentPlayersCount() {
        return damageListenerList.size();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Health getHealth() {
        return health;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    public Mana getMana() {
        return mana;
    }

    public boolean isDead(){
        return !getHealth().hasHealth();
    }

    public boolean isAlive(){
        return getHealth().hasHealth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        String playerInfo = isDead() ? "[DEAD]" : String.format("%s -- %s -- Deck Capacity: %d", getHealth(), getMana(), getDeck().getCardCount());
        return String.format("ID: %d -- Player %s -- %s", id, name, playerInfo);
    }
}
