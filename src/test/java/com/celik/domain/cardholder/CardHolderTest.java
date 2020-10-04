package com.celik.domain.cardholder;

import com.celik.exception.DoesNotExistException;
import com.celik.exception.HasNoCapacityException;
import com.celik.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class CardHolderTest {

    CardHolder cardHolderImpl = null;

    @BeforeEach
    public void initializeCardHolder(){
        cardHolderImpl = Mockito.mock(CardHolder.class, Mockito.withSettings()
                                        .useConstructor()
                                        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    // init
    @Test
    public void whenCardHolderIsCreated_cardsShouldBeEmpty(){
        Assertions.assertNotNull(cardHolderImpl.getCards());
        Assertions.assertEquals(0, cardHolderImpl.getCards().size());
    }


    // addCard
    @Test
    public void whenHavingCapacity_cardShouldBeAdded(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        //act
        Card card = new Card(5);
        Executable addCard = () -> cardHolderImpl.addCard(card);

        // assert
        Assertions.assertDoesNotThrow(addCard);
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());
    }

    @Test
    public void whenNotHavingCapacity_shouldThrowHasNoCapacityError(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(false);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        //act
        Card card = new Card(5);
        Executable addCard = () -> cardHolderImpl.addCard(card);

        // assert
        Assertions.assertThrows(HasNoCapacityException.class, addCard);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());
    }

    // hasCard

    @Test
    public void whenHolderHasCard_hasCardShouldReturnTrue(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        Card card = new Card(5);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

        // assert
        Assertions.assertTrue(cardHolderImpl.hasCard(card));
    }

    @Test
    public void whenHolderHasNotCard_hasCardShouldReturnFalse(){

        // arrange
        cardHolderImpl.reset();
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        Card card = new Card(5);

        // assert
        Assertions.assertFalse(cardHolderImpl.hasCard(card));
    }

    // getCards
    @Test
    public void whenGetCards_shouldNotBeModifiable(){

        // arrange
        Set<Card> cards = cardHolderImpl.getCards();

        //act- assert
        Card card = new Card(5);
        Assertions.assertNotNull(cards);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> cards.add(card));
    }

    // isEmpty
    @Test
    public void whenHolderHasNotAnyCard_isEmptyShouldBeTrue(){

        // arrange
        cardHolderImpl.reset();
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        //assert
        Assertions.assertTrue(cardHolderImpl.isEmpty());
    }

    @Test
    public void whenHolderContainsCards_isEmptyShouldBeFalse(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        Card card = new Card(5);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

        //assert
        Assertions.assertFalse(cardHolderImpl.isEmpty());
    }

    // reset
    @Test
    public void givenHolderHasCards_whenReset_shouldBeEmpty(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        Card card = new Card(5);
        Card card2 = new Card(6);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card2));
        Assertions.assertEquals(2, cardHolderImpl.getCardCount());
        Assertions.assertFalse(cardHolderImpl.isEmpty());

        // act
        cardHolderImpl.reset();

        // assert
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());
        Assertions.assertTrue(cardHolderImpl.isEmpty());
    }

    // getCardCount
    @Test
    public void getCardCountShouldReturnActualSize(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

        Card card = new Card(5);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

        Card card2 = new Card(6);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card2));
        Assertions.assertEquals(2, cardHolderImpl.getCardCount());

        cardHolderImpl.reset();
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());
    }

    // giveCard

    @Test
    public void whenInvokeGiveCardWithNotExistCard_shouldThrowDoesNotExists(){

        // arrange
        Card card = new Card(5);
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);

        Card card2 = new Card(6);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card2));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

        //act
        Assertions.assertThrows(DoesNotExistException.class, () -> cardHolderImpl.giveCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

    }

    @Test
    public void whenInvokeGiveCardWithExistCard_shouldRemoveAndReturnCard(){

        // arrange
        Card card = new Card(6);
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());
        Assertions.assertTrue(cardHolderImpl.hasCard(card));


        //act
        AtomicReference<Card> givenCard = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            givenCard.set(cardHolderImpl.giveCard(card));
        });

        //assert
        Assertions.assertNotNull(givenCard.get());
        Assertions.assertEquals(card, givenCard.get());
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());
    }

    @Test
    public void whenInvokeGiveCardWithInvalidCardId_shouldThrowDoesNotExistException(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);

        Card card = new Card(5);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

        //act
        int invalidCardId = card.getId() + 1;
        Assertions.assertThrows(DoesNotExistException.class, () -> cardHolderImpl.giveCard(invalidCardId));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());

    }

    @Test
    public void whenInvokeGiveCardWithValidCardId_shouldRemoveAndReturnCard(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);

        Card card = new Card(6);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());
        Assertions.assertTrue(cardHolderImpl.hasCard(card));

        //act;
        AtomicReference<Card> givenCard = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            givenCard.set(cardHolderImpl.giveCard(card.getId()));
        });

        // assert
        Assertions.assertNotNull(givenCard.get());
        Assertions.assertEquals(card, givenCard.get());
        Assertions.assertEquals(0, cardHolderImpl.getCardCount());

    }

    @Test
    public void whenAnotherHasCapacity_shouldGivesCardToAnother(){

        // arrange
        Card card = new Card(5);
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));

        CardHolder anotherCardHolder =  Mockito.mock(CardHolder.class, Mockito.withSettings()
                .useConstructor()
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Mockito.when(anotherCardHolder.hasCapacity()).thenReturn(true);

        // act
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.giveCard(card, anotherCardHolder));

        //assert
        Assertions.assertFalse(cardHolderImpl.hasCard(card));
        Assertions.assertTrue(anotherCardHolder.hasCard(card));
    }

    @Test
    public void whenAnotherHasNotCapacity_shouldThrowsHasNoCapacity(){

        // arrange
        Card card = new Card(5);
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));

        CardHolder anotherCardHolder =  Mockito.mock(CardHolder.class, Mockito.withSettings()
                .useConstructor()
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Mockito.when(anotherCardHolder.hasCapacity()).thenReturn(false);

        // act
        Assertions.assertThrows(HasNoCapacityException.class, () -> cardHolderImpl.giveCard(card, anotherCardHolder));

        //assert
        Assertions.assertTrue(cardHolderImpl.hasCard(card));
        Assertions.assertFalse(anotherCardHolder.hasCard(card));
    }

    @Test
    public void whenSourceCardHolderHasNotCard_shouldThrowsDoesNotExistException(){

        // arrange
        Card card = new Card(5);
        Assertions.assertFalse(cardHolderImpl.hasCard(card));

        CardHolder anotherCardHolder =  Mockito.mock(CardHolder.class, Mockito.withSettings()
                .useConstructor()
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Mockito.when(anotherCardHolder.hasCapacity()).thenReturn(true);

        // act
        Assertions.assertThrows(DoesNotExistException.class, () -> cardHolderImpl.giveCard(card, anotherCardHolder));

        //assert
        Assertions.assertFalse(cardHolderImpl.hasCard(card));
        Assertions.assertFalse(anotherCardHolder.hasCard(card));
    }

    // findbyId
    @Test
    public void whenFindByInvalidId_shouldReturnEmptyOptional(){

        // act
        int invalidCardId = 1; // does not exist any card with this id
        Optional<Card> foundCard = cardHolderImpl.findCardById(invalidCardId);

        // assert
        Assertions.assertTrue(foundCard.isEmpty());
    }

    @Test
    public void whenFindByExistingId_shouldReturnCard(){

        // arrange
        Mockito.when(cardHolderImpl.hasCapacity()).thenReturn(true);

        Card card = new Card(6);
        Assertions.assertDoesNotThrow(() -> cardHolderImpl.addCard(card));
        Assertions.assertEquals(1, cardHolderImpl.getCardCount());
        Assertions.assertTrue(cardHolderImpl.hasCard(card));

        // act
        Optional<Card> foundCard = cardHolderImpl.findCardById(card.getId());

        // assert
        Assertions.assertTrue(foundCard.isPresent());
        Assertions.assertEquals(card, foundCard.get());
    }

}
