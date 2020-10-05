package com.celik;

import com.celik.constants.GameConstants;
import com.celik.domain.Health;
import com.celik.domain.cardholder.Deck;
import com.celik.domain.mana.Mana;
import com.celik.exception.*;
import com.celik.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class TradingCardGame {

    static Logger logger = LoggerFactory.getLogger(TradingCardGame.class);

    protected List<Player> players;
    protected Integer activePlayerIndex;

    public TradingCardGame(){
        players = new ArrayList<>();
        activePlayerIndex = 0;
        logger.info("Game initialized");
    }

    public void play() throws DeadPlayerException, DoesNotExistException, InsufficientAmountException {
        if( players.size() < GameConstants.MINIMUM_PLAYER_COUNT_TO_PLAY) {
            throw new InsufficientAmountException(String.format("Min %d players are required to play",
                                                                        GameConstants.MINIMUM_PLAYER_COUNT_TO_PLAY));
        }

        this.getPlayers().forEach(this::drawInitialCardsOfPlayer);

        activatePlayer(activePlayerIndex);
        while( !isOver() ) {
            try {
                playRound();
            } catch (TurnIsOverException e) {
                setNextTurnPlayerIndex();
                activatePlayer(activePlayerIndex);
            } finally {
                nextRound();
            }
        }
    }

    protected abstract void playRound() throws TurnIsOverException, DeadPlayerException, DoesNotExistException;
    protected abstract void nextRound();
    protected abstract void notifyPlayer(String message);

    protected Player preparePlayerForGame(String name) {
        Deck deck = Deck.getDeckWithManaCosts(GameConstants.INITIAL_MANA_COSTS_OF_DECK);
        Health health = new Health(GameConstants.INITIAL_PLAYER_HEALTH);
        return new Player(name, health, Mana.getEmptyMana(), deck);
    }

    private void drawInitialCardsOfPlayer(Player player){
        IntStream.range(0, GameConstants.INITIAL_CARD_DRAW_COUNT).forEach(i -> drawNewCardToHand(player) );
    }

    public void addPlayer(String name){
        Player newPlayer = preparePlayerForGame(name);

        players.stream().forEach( existingPlayer -> {
            existingPlayer.addOpponentPlayer(newPlayer);
            newPlayer.addOpponentPlayer(existingPlayer);
        });
        players.add(newPlayer);

        logger.info("Player {} was added to game", newPlayer.getName());
    }

    public Optional<Player> getWinner(){
        if( !isOver() ) return Optional.empty();
        return players.stream().filter(player -> player.isAlive()).findFirst();
    }

    protected void playCardWithActivePlayer(int cardId) throws DeadPlayerException, DoesNotExistException, InsufficientAmountException {
        Player activePlayer = getActivePlayer();
        activePlayer.playCard(cardId);
    }

    protected Player getActivePlayer() {
        return players.size() > activePlayerIndex ? players.get(activePlayerIndex) : null;
    }

    protected boolean isOver(){
        return getAlivePlayerCount() < 2;
    }

    private int getAlivePlayerCount(){
        return Long.valueOf(players.stream().filter(player -> player.isAlive()).count()).intValue();
    }

    protected List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    // activate a player
    protected void activatePlayer(int playerIndex) {
        if( players.size() <= playerIndex ) return;

        Player player = players.get(playerIndex);
        addManaSlotToPlayer(player);
        player.getMana().refillManaSlots();
        drawNewCardToHand(player);
    }

    private void addManaSlotToPlayer(Player player) {
        try {
            player.getMana().addManaSlot();
        } catch (HasNoCapacityException e) {
            logger.info("Player {} has not mana slot capacity.", player.getId());
        }
    }

    private void drawNewCardToHand(Player player) {
        try {
            player.drawCard();
        } catch (EmptyResourceUsingException e) {
            logger.info("Player {}'s deck has not any card to draw new card!", player.getId());
           bleedingOut(player);
        } catch (HasNoCapacityException e) {
            logger.info("Player {}'s hand has not card capacity to draw new card!", player.getId());
            notifyPlayer("OVERLOAD! You can not draw a new card when the hand is full.");
        }
    }

    private void bleedingOut(Player player){
        player.takeDamage(GameConstants.EMPTY_DECK_DAMAGE);
        notifyPlayer("Bleeding OUT! You was damaged cause your deck is empty before end of game.");
    }

    // change active player
    private void setNextTurnPlayerIndex() {
        int nextTurnIndex = activePlayerIndex;
        do {
            nextTurnIndex = nextTurnIndex + 1;
            if( nextTurnIndex >= players.size() ) nextTurnIndex = 0;
        } while (!isOver() && players.get(nextTurnIndex).isDead());
        activePlayerIndex = nextTurnIndex;
    }
}
