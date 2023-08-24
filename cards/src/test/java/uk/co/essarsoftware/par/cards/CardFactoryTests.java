package uk.co.essarsoftware.par.cards;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CardFactory}.
 * @author essar
 */
public class CardFactoryTests
{

    @Test
    public void testInitializationIsSuccessful() {

        assertNotNull(new CardFactory(), "Expected object initialization to be successful");
        
    }

    @Test
    public void testAllCardsReturns52Cards() {

        Card[] cards = CardFactory.allCards();
        assertEquals(52, cards.length, "Card array should contain 52 cards");

    }

    @Test
    public void testForSuitReturns13Cards() {

        Card[] cards = CardFactory.forSuit(Suit.CLUBS);
        assertEquals(13, cards.length, "Card array of CLUBS should contain 13 cards");

    }

    @Test
    public void testForSuitReturns13Values() {

        Card[] cards = CardFactory.forSuit(Suit.CLUBS);
        Value[] values = Arrays.stream(cards).map(Card::getValue).distinct().toArray(Value[]::new);
        assertEquals(13, values.length, "Card array of CLUBS should contain 13 distinct values");

    }

    @Test
    public void testForSuitWithNullSuitReturnsEmptyArray() {

        Card[] cards = CardFactory.forSuit(null);
        assertArrayEquals(new Card[0], cards, "Expected an empty array");

    }

    @Test
    public void testForValueReturns4Cards() {

        Card[] cards = CardFactory.forValue(Value.ACE);
        assertEquals(4, cards.length, "Card array of ACEs should contain 4 cards");

    }

    @Test
    public void testForValueReturns4Suits() {

        Card[] cards = CardFactory.forValue(Value.ACE);
        Suit[] suits = Arrays.stream(cards).map(Card::getSuit).distinct().toArray(Suit[]::new);
        assertEquals(4, suits.length, "Card array of ACEs should contain 4 distinct suits");

    }

    @Test
    public void testForValueWithNullValueReturnsEmptyArray() {

        Card[] cards = CardFactory.forValue(null);
        assertArrayEquals(new Card[0], cards, "Expected an empty array");

    }

    @Test
    public void testWithPreviousValueReturnsCardOfSameSuit() {

        Card card = CardFactory.withPreviousValue(TestCardFactory.card(Suit.CLUBS, Value.TWO));
        assertEquals(Suit.CLUBS, card.getSuit(), "Expected card to have CLUBS suit");

    }

    @Test
    public void testWithPreviousValueReturnsCardWithLowerValue() {

        Card card = CardFactory.withPreviousValue(TestCardFactory.card(Suit.CLUBS, Value.TWO));
        assertEquals(Value.ACE, card.getValue(), "Expected card to have ACE value");

    }

    @Test
    public void testWithPreviousValueReturnsNullForCardWithNoLowerValue() {

        Card card = CardFactory.withPreviousValue(TestCardFactory.card(Suit.CLUBS, Value.ACE));
        assertNull(card, "Expected null as no lower value");

    }

    @Test
    public void testWithNextValueReturnsCardOfSameSuit() {

        Card card = CardFactory.withNextValue(TestCardFactory.card(Suit.CLUBS, Value.TWO));
        assertEquals(Suit.CLUBS, card.getSuit(), "Expected card to have CLUBS suit");

    }

    @Test
    public void testWithNextValueReturnsCardWithHigherValue() {

        Card card = CardFactory.withNextValue(TestCardFactory.card(Suit.CLUBS, Value.TWO));
        assertEquals(Value.THREE, card.getValue(), "Expected card to have THREE value");

    }

    @Test
    public void testWithNextValueReturnsNullForCardWithNoHigherValue() {

        Card card = CardFactory.withNextValue(TestCardFactory.card(Suit.CLUBS, Value.KING));
        assertNull(card, "Expected null as no higher value");
        
    }
}
