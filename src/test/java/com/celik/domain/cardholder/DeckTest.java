package com.celik.domain.cardholder;

import com.celik.exception.EmptyResourceUsingException;
import com.celik.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeckTest extends CardHolderTest {

    Deck deck;

    @BeforeEach
    @Override
    public void initializeCardHolder() {
        cardHolderImpl = Mockito.mock(Deck.class, Mockito.withSettings().useConstructor().defaultAnswer(Answers.CALLS_REAL_METHODS));
        deck = (Deck) cardHolderImpl;
    }

    @Test
    public void whenCreateDeckWithManaCosts_allCostShouldExistInDeck(){
        // arrange
        List<Integer> manaCosts = List.of(1,2,3,4,5);

        //act
        deck = Deck.getDeckWithManaCosts(manaCosts);

        //assert
        Assertions.assertEquals(manaCosts.size(), deck.getCards().size());
        deck.getCards().stream().forEach(card -> {
            Assertions.assertTrue(manaCosts.contains(card.getManaCost()));
        });
    }

    @Test
    public void whenPickedRandomCard_shouldNotChangeSizeOfCards() {

        // arrange
        int addingCount = 3;
        List<Card> addedCards = addCardWithQuantity(addingCount);
        Assertions.assertEquals(addingCount, cardHolderImpl.getCardCount());

        // act
        Assertions.assertDoesNotThrow(() -> deck.pickRandomCard());

        //assert
        Assertions.assertEquals(addingCount, deck.getCardCount());
    }

    @Test
    public void whenPickedRandomCard_deckGivesCard() {

        // arrange
        int addingCount = 3;
        List<Card> addedCards = addCardWithQuantity(addingCount);

        // act
        AtomicReference<Card> pickedCard = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            pickedCard.set(deck.pickRandomCard());
        });

        //assert
        Assertions.assertNotNull(pickedCard.get());
        Assertions.assertTrue(addedCards.contains(pickedCard.get()));
        Assertions.assertTrue(deck.hasCard(pickedCard.get()));
    }

    @Test
    public void givenDeckHasNoCard_whenPickRandomCard_thenThrowsEmptyResourceUsingException() {

        // arrange
        Assertions.assertEquals(0, deck.getCardCount());

        // act
        Assertions.assertThrows(EmptyResourceUsingException.class, () -> deck.pickRandomCard());
    }

    // helpers
    private List<Card> addCardWithQuantity(int addingCount){
        List<Card> addedCards = IntStream.range(0, addingCount)
                .mapToObj(addAndReturnCard)
                .collect(Collectors.toList());

        Assertions.assertEquals(addingCount, deck.getCardCount());
        return addedCards;
    }

    private IntFunction<Card> addAndReturnCard = manaCost -> {
        Card card = new Card(manaCost);
        Assertions.assertDoesNotThrow(() -> deck.addCard(card));
        Assertions.assertTrue(deck.hasCard(card));
        return card;
    };
}
