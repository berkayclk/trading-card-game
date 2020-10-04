package com.celik.domain.cardholder;

import com.celik.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class HandTest extends CardHolderTest{

    Hand hand;

    @BeforeEach
    @Override
    public void initializeCardHolder() {
        cardHolderImpl = Mockito.mock(Hand.class, Mockito.withSettings().useConstructor().defaultAnswer(Answers.CALLS_REAL_METHODS));
        hand = (Hand) cardHolderImpl;
    }

    private IntConsumer addCardWithoutException = manaCost -> Assertions.assertDoesNotThrow(() -> hand.addCard(new Card(manaCost)));

    @Test
    public void whenCardCountIsMaxCount_hasCapacityShouldReturnFalse (){

        // arrange
        IntStream.range(0, Hand.MAX_CARD_COUNT).forEach(addCardWithoutException);
        Assertions.assertEquals(Hand.MAX_CARD_COUNT, hand.getCardCount());

        //act -assert
        Assertions.assertFalse(hand.hasCapacity());
    }

    @Test
    public void whenCardCountIsLessThanMaxCount_hasCapacityShouldReturnTrue (){

        // arrange
        int addingCount = Hand.MAX_CARD_COUNT - 1;
        IntStream.range(0, addingCount).forEach(addCardWithoutException);
        Assertions.assertEquals(addingCount, hand.getCardCount());

        //act -assert
        Assertions.assertTrue(hand.hasCapacity());
    }


}
