package com.celik;

import com.celik.domain.Health;
import com.celik.domain.cardholder.Deck;
import com.celik.domain.mana.Mana;
import com.celik.exception.InsufficientAmountException;
import com.celik.exception.TradingCardException;
import com.celik.exception.TurnIsOverException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TradingCardGameTest {

    TradingCardGame game;

    @BeforeEach
    public void initializeTradingCardGame(){
        game = Mockito.mock(TradingCardGame.class, Mockito.withSettings()
                                                    .useConstructor()
                                                    .defaultAnswer(Answers.CALLS_REAL_METHODS));
    }

    @Test
    public void whenAddPlayer_shouldHaveInitialDeck(){
        // arrange
        game.addPlayer("Test");
        Player player = game.getPlayers().stream().filter(p -> p.getName().equals("Test")).findFirst().get();

        List<Integer> actualManaCosts = player.getDeck().getCards().stream().map(card -> card.getManaCost()).collect(Collectors.toList());
        actualManaCosts.sort(Integer::compareTo);

        // assert
        List<Integer> initialManaCosts = List.of(0,0,1,1,2,2,2,3,3,3,3,4,4,4,5,5,6,6,7,8);
        Assertions.assertIterableEquals(initialManaCosts, actualManaCosts);
    }

    @Test
    public void whenAddPlayer_shouldHaveThirtyHealth(){
        // arrange
        game.addPlayer("Test");
        Player player = game.getPlayers().stream().filter(p -> p.getName().equals("Test")).findFirst().get();

        // assert
        Assertions.assertEquals(30, player.getHealth().getHealthValue());
    }

    @Test
    public void whenAddPlayer_shouldHaveEmptyHand(){
        // arrange
        game.addPlayer("Test");
        Player player = game.getPlayers().stream().filter(p -> p.getName().equals("Test")).findFirst().get();

        // assert
        Assertions.assertTrue(player.getHand().isEmpty());
    }

    @Test
    public void whenThereIsNoPlayer_playMethodShouldThrowInsufficientAmountException(){
        Assertions.assertEquals(0, game.getPlayers().size());
        Assertions.assertThrows(InsufficientAmountException.class,() -> game.play());
    }

    @Test
    public void whenThereIsOnePlayer_playMethodShouldThrowInsufficientAmountException(){
        game.addPlayer("new player");
        Assertions.assertEquals(1, game.getPlayers().size());
        Assertions.assertThrows(InsufficientAmountException.class,() -> game.play());
    }

    @Test
    public void whenThereAreMoreThanTwoPlayers_playMethodShouldNotThrowsInsufficientAmountException(){
        Mockito.when(game.isOver()).thenReturn(true);

        game.addPlayer("new player");
        game.addPlayer("new player");
        Assertions.assertEquals(2, game.getPlayers().size());
        Assertions.assertDoesNotThrow(() -> game.play());
    }

    @Test
    public void whenThereAreMoreThanTwoPlayers_allPlayersShouldHaveThreeCardOnHand(){
        // arrange
        game.addPlayer("Test");
        game.addPlayer("Test 2");

        // act
        Mockito.doReturn(true).when(game).isOver();
        Mockito.doNothing().when(game).activatePlayer(Mockito.anyInt());
        Assertions.assertDoesNotThrow(() -> game.play());

        // assert
        game.getPlayers().forEach(player -> {
            Assertions.assertFalse(player.getHand().isEmpty());
            Assertions.assertEquals(3, player.getHand().getCardCount());
        });
    }

    @Test
    public void whenGameIsNotOver_getWinner_shouldReturnEmpty(){

        // arrange
        Mockito.when(game.isOver()).thenReturn(true);

        game.addPlayer("new player");
        Optional<Player> player = game.getPlayers().stream().findFirst();

        // act assert
        Assertions.assertEquals(player, game.getWinner());
    }

    @Test
    public void givenPlayerHasNotCardInDeck_whenAddPlayer_shouldInvokeDrawCardThreeTimes(){

        // arrange
        Player player = Mockito.mock(Player.class, Mockito.withSettings()
                                                    .useConstructor("test", new Health(10), Mana.getEmptyMana(), new Deck())
                                                    .defaultAnswer(Answers.CALLS_REAL_METHODS));
        Mockito.doReturn(player).when(game).preparePlayerForGame("test");

        game.addPlayer("test");
        game.addPlayer("test 2");

        Mockito.doReturn(true).when(game).isOver();
        Mockito.doNothing().when(game).activatePlayer(Mockito.anyInt());
        Assertions.assertDoesNotThrow(() -> game.play());

        // act
        try {
            Mockito.verify(player, Mockito.times(3)).drawCard();
        } catch (TradingCardException e) {
           Assertions.fail(e);
        }
    }

    @Test
    public void givenPlayerHasNotCardInDeck_whenDrawCard_shouldTakeDamage(){

        // arrange
        Player player = Mockito.mock(Player.class, Mockito.withSettings()
                                                    .useConstructor("test", new Health(10), Mana.getEmptyMana(), new Deck())
                                                    .defaultAnswer(Answers.CALLS_REAL_METHODS));
        Mockito.doReturn(player).when(game).preparePlayerForGame("test");

        game.addPlayer("test");
        game.addPlayer("test 2");

        Mockito.doReturn(true).when(game).isOver();
        Mockito.doNothing().when(game).activatePlayer(Mockito.anyInt());
        Assertions.assertDoesNotThrow(() -> game.play());

        // act - assert
        Mockito.doNothing().when(game).activatePlayer(Mockito.anyInt());
        Mockito.verify(player, Mockito.times(3)).takeDamage(1);
    }

    @Test
    public void whenGameIsStarted_shouldActivateFirstPlayer(){

        // arrange
        game.addPlayer("test");
        game.addPlayer("test 2");

        try {
            Mockito.doNothing().when(game).playRound();
            Mockito.doNothing().when(game).nextRound();

            // return true in second call
            Mockito.doReturn(true).when(game).isOver();
        } catch (TradingCardException e) {
            Assertions.fail(e);
        }

        // act
        Assertions.assertDoesNotThrow(game::play);

        //assert
        int initialActiveIndex = 0;
        Mockito.verify(game, Mockito.times(1)).activatePlayer(initialActiveIndex);
    }

    @Test
    public void whenGameIsNotOverTimes_shouldCallsRoundMethods(){

        // arrange
        game.addPlayer("test");
        game.addPlayer("test 2");

        try {
            Mockito.doNothing().when(game).playRound();
            Mockito.doNothing().when(game).nextRound();

            // return true in second call
            Mockito.doReturn(false).doReturn(true).when(game).isOver();
        } catch (TradingCardException e) {
            Assertions.fail(e);
        }

        // act
        Assertions.assertDoesNotThrow(game::play);

        //assert
        try {
            Mockito.verify(game, Mockito.times(1)).nextRound();
            Mockito.verify(game, Mockito.times(1)).playRound();
        }  catch (TradingCardException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void whenRoundThrowsTurnIsOverException_changesActivePlayer(){

        // arrange
        game.addPlayer("test");
        game.addPlayer("test 2");

        try {
            Mockito.doNothing().when(game).playRound();
            Mockito.doNothing().when(game).nextRound();

            Mockito.doReturn(false)
                    .doReturn(false)
                    .doReturn(false)
                    .doReturn(true)
                    .when(game).isOver();

            Mockito.doThrow(TurnIsOverException.class)
                    .doNothing()
                    .when(game).playRound();
        } catch (TradingCardException e) {
            Assertions.fail(e);
        }

        // act
        Assertions.assertDoesNotThrow(game::play);

        // assert
        try {
            Mockito.verify(game, Mockito.times(2)).nextRound();
            Mockito.verify(game, Mockito.times(2)).playRound();

            Mockito.verify(game, Mockito.times(1)).activatePlayer(0);
            Mockito.verify(game, Mockito.times(1)).activatePlayer(1);

        }  catch (TradingCardException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void whenTurnOfLastPlayerIsOver_changesTurnAsFirstPlayer(){

        // arrange
        game.addPlayer("test");
        game.addPlayer("test 2");

        try {
            Mockito.doNothing().when(game).playRound();
            Mockito.doNothing().when(game).nextRound();

            Mockito.doReturn(false)
                    .doReturn(false)
                    .doReturn(false)
                    .doReturn(true)
                    .when(game).isOver();

            Mockito.doThrow(TurnIsOverException.class)
                    .doThrow(TurnIsOverException.class)
                    .when(game).playRound();
        } catch (TradingCardException e) {
            Assertions.fail(e);
        }

        // act
        Assertions.assertDoesNotThrow(game::play);

        // assert
        try {
            Mockito.verify(game, Mockito.times(2)).nextRound();
            Mockito.verify(game, Mockito.times(2)).playRound();

            Mockito.verify(game, Mockito.times(2)).activatePlayer(0);
            Mockito.verify(game, Mockito.times(1)).activatePlayer(1);

        }  catch (TradingCardException e) {
            Assertions.fail(e);
        }
    }
}
