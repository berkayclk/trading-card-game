package com.celik;

import com.celik.domain.Health;
import com.celik.domain.cardholder.Deck;
import com.celik.domain.cardholder.Hand;
import com.celik.domain.damage.DamageEvent;
import com.celik.domain.mana.Mana;
import com.celik.exception.*;
import com.celik.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import java.util.Optional;

public class PlayerTest {

    Player player = null;
    Deck deck;
    Mana mana;
    Health health;
    Hand hand;

    @BeforeEach
    public void initPlayer(){
        deck = Mockito.mock(Deck.class);
        mana = Mockito.mock(Mana.class);
        health = Mockito.mock(Health.class);
        hand = Mockito.mock(Hand.class);

        player = Mockito.mock(Player.class, Mockito.withSettings()
                                                .useConstructor("Test", health, mana, deck)
                                                .defaultAnswer(Answers.CALLS_REAL_METHODS));
    }

    // drawCard
    @Test
    public void whenPickRandomCardThrowsEmptyResourceUsingException_throwsEmptyResourceUsingException(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);

        try {
            Mockito.when(deck.pickRandomCard()).thenThrow(EmptyResourceUsingException.class);
        } catch (EmptyResourceUsingException e) {
            Assertions.fail(e);
        }

        //act
        Assertions.assertThrows(EmptyResourceUsingException.class, () -> player.drawCard());

    }

    @Test
    public void whenGiveCardThrowsDoesNotExistException_throwsEmptyResourceUsingException(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);

        hand = player.getHand();
        Card pickedCard = new Card(5);

        try {
            Mockito.when(deck.pickRandomCard()).thenReturn(pickedCard);
            Mockito.doThrow(DoesNotExistException.class).when(deck).giveCard(pickedCard, hand);
        } catch (EmptyResourceUsingException | DoesNotExistException | HasNoCapacityException e) {
            Assertions.fail(e);
        }

        //act
        Assertions.assertThrows(EmptyResourceUsingException.class, () -> player.drawCard());

    }

    @Test
    public void whenDeckDoesNotThrow_drawCardShouldNotThrow(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);

        hand = player.getHand();
        Card pickedCard = new Card(5);

        try {
            Mockito.when(deck.pickRandomCard()).thenReturn(pickedCard);
            Mockito.doNothing().when(deck).giveCard(pickedCard, hand);
        } catch (EmptyResourceUsingException | DoesNotExistException | HasNoCapacityException e) {
            Assertions.fail(e);
        }

        //act
        Assertions.assertDoesNotThrow(() -> player.drawCard());
    }

    // playCard
    @Test
    public void givenPlayerWithoutHealth_whenPlayCard_throwsDeadPlayerException(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(false);

        //act
        Assertions.assertThrows(DeadPlayerException.class, () -> player.playCard(5));
    }

    @Test
    public void givenHandWithoutCard_whenPlayCard_throwsDoesNotExistException(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);
        Mockito.when(player.getHand()).thenReturn(hand);
        Mockito.when(hand.findCardById(5)).thenReturn(Optional.empty());

        //act
        Assertions.assertThrows(DoesNotExistException.class, () -> player.playCard(5));
    }

    @Test
    public void givenHandWithCard_whenPlayCardWithZeroMana_dontInvokeUseMana(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);
        Mockito.when(player.getHand()).thenReturn(hand);

        Card card = new Card(0);
        try {
            Mockito.when(hand.findCardById(card.getId())).thenReturn(Optional.of(card));
            Mockito.when(hand.giveCard(card)).thenReturn(card);
        } catch (DoesNotExistException e) {
            Assertions.fail(e);
        }

        //act
        try {
            Mockito.verify(mana, Mockito.times(0)).useMana(0);
            Assertions.assertDoesNotThrow(() -> player.playCard(card.getId()));
        } catch (InsufficientAmountException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void givenHandWithCard_whenPlayCardWithPositiveMana_invokeUseMana(){
        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);
        Mockito.when(player.getHand()).thenReturn(hand);

        Card card = new Card(5);
        try {
            Mockito.when(hand.findCardById(card.getId())).thenReturn(Optional.of(card));
            Mockito.when(hand.giveCard(card)).thenReturn(card);
        } catch (DoesNotExistException e) {
            Assertions.fail(e);
        }

        //act - assert
        try {
            Assertions.assertDoesNotThrow(() -> player.playCard(card.getId()));
            Mockito.verify(mana, Mockito.times(1)).useMana(card.getManaCost());
        } catch (InsufficientAmountException e) {
            Assertions.fail(e);
        }
        Assertions.assertDoesNotThrow(() -> player.playCard(card.getId()));
    }

    @Test
    public void givenHandWithCard_whenPlayCardWithPositive_inflictDamageToOpponents(){

        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);
        Mockito.when(player.getHand()).thenReturn(hand);

        Card card = new Card(5);
        try {
            Mockito.when(hand.findCardById(card.getId())).thenReturn(Optional.of(card));
            Mockito.when(hand.giveCard(card)).thenReturn(card);
        } catch (DoesNotExistException e) {
            Assertions.fail(e);
        }

        Player opponent = Mockito.mock(Player.class, Mockito.withSettings()
                                .useConstructor("Test 2", health, mana, deck)
                                .defaultAnswer(Answers.CALLS_REAL_METHODS));
        player.addOpponentPlayer(opponent);

        //act - assert
        Assertions.assertDoesNotThrow(() -> player.playCard(card.getId()));
        Mockito.verify(opponent, Mockito.times(1)).takeDamage(Mockito.any(DamageEvent.class));

    }

    // takeDamage
    @Test
    public void givenHealthWithZeroValue_whenTakeDamage_dontInvokeDecreaseHealth(){

        // arrange
        Mockito.when(health.hasHealth()).thenReturn(false);

        //act
        player.takeDamage(5);

        // assert
        try {
            Mockito.verify(health, Mockito.times(0)).decreaseHealth(Mockito.anyInt());
        } catch (InsufficientAmountException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void givenHealthWithPositiveValue_whenTakeDamage_invokeDecreaseHealth(){

        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);

        //act
        player.takeDamage(5);

        // assert
        try {
            Mockito.verify(health, Mockito.times(1)).decreaseHealth(5);
        } catch (InsufficientAmountException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void givenHealthThrowsInsufficientAmountException_whenTakeDamage_clearOpponentPlayers(){

        // arrange
        Mockito.when(health.hasHealth()).thenReturn(true);
        try {
            Mockito.doThrow(InsufficientAmountException.class).when(health).decreaseHealth(5);
        } catch (InsufficientAmountException e) {
            Assertions.fail(e);
        }

        Player opponent = Mockito.mock(Player.class, Mockito.withSettings()
                .useConstructor("Test 2", health, mana, deck)
                .defaultAnswer(Answers.CALLS_REAL_METHODS));
        player.addOpponentPlayer(opponent);
        Assertions.assertEquals(1, player.getOpponentPlayersCount());

        // act
        player.takeDamage(5);

        // assert
        Assertions.assertEquals(0, player.getOpponentPlayersCount());

    }

}
