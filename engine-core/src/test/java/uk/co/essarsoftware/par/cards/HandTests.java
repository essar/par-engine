package uk.co.essarsoftware.par.cards;

import static uk.co.essarsoftware.par.cards.TestCardFactory.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Hand}.
 * @author essar
 */
public class HandTests
{


    // Test utility methods

    private Hand createHand(Card... cards) {

        Hand hand = new Hand();
        Arrays.stream(cards).forEach(hand::addCard);
        return hand;

    }

    // Hand tests

    @Test
    public void testAddCardToEmptyHand() {

        Card card = card();
        Hand hand = createHand();
        hand.addCard(card);
        assertArrayEquals(asArray(card), hand.getCards(), "Card should be contained in Hand");

    }

    @Test
    public void testAddCardFailsIfCardExistsInHand() {

        Card card = card();
        Hand hand = createHand(card);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(card), "Should raise IllegalArgumentException");

    }

    @Test
    public void testAddBoundJokerFailsIfBoundCardExistsInHand() {

        Card card = card();
        Hand hand = createHand(card);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(boundJoker(card)), "Should raise IllegalArgumentException");

    }

    @Test
    public void testAddCardFailsIfSameCardExistsInHand() {

        Card card = card();
        Hand hand = createHand(card);
        assertThrows(IllegalArgumentException.class, () -> hand.addCard(copyOf(card)), "Should raise IllegalArgumentException");

    }

    @Test
    public void testAddMultipleUnboundJokers() {

        Joker joker1 = unboundJoker();
        Joker joker2 = unboundJoker();
        Hand hand = createHand(joker1);
        hand.addCard(joker2);
        assertArrayEquals(asArray(joker1, joker2), hand.getCards(), "Both jokers should be contained in Hand");

    }

    @Test
    public void testNewHandContainsNoCards() {

        Hand hand = new Hand();
        assertArrayEquals(emptyArray(), hand.getCards(), "New Hand should contain no cards");

    }

    @Test
    public void testNewHandHasZeroSize() {

        Hand hand = new Hand();
        assertEquals(0, hand.size(), "New Hand should have 0 size");

    }

    @Test
    public void testFindCardInEmptyHand() {

        Hand hand = createHand();
        assertNull(hand.findCard(card()), "No card found; null should be returned");

    }

    @Test
    public void testFindCardThatExistsInHand() {

        Card card = card();
        Hand hand = createHand(unboundJoker(), card(card.getSuit(), altCard().getValue()), card(altCard().getSuit(), card.getValue()), card);
        Card foundCard = hand.findCard(card);
        assertEquals(card, foundCard, "Card found and returned");
        
    }

    @Test
    public void testFindCardWithSameCardInHand() {

        Card card = card();
        Hand hand = createHand(card);
        Card foundCard = hand.findCard(copyOf(card));
        assertEquals(card, foundCard, "Card found and returned");
        
    }

    @Test
    public void testFindCardForBoundJokerWithSameSuitAndValue() {

        Card card = card();
        Hand hand = createHand(card);
        Card foundCard = hand.findCard(boundJoker(copyOf(card)));
        assertEquals(card, foundCard, "Card found and returned");
        
    }

    @Test
    public void testFindUnboundJoker() {

        Hand hand = createHand(boundJoker(card()), unboundJoker());
        Card foundCard = hand.findCard(unboundJoker());
        assertTrue(foundCard.isJoker(), "Card found should be a joker");
        assertFalse(((Joker) foundCard).isBound(), "Card found should be unbound");
        
    }

    @Test
    public void testFindUnboundJokerNotInHand() {

        Hand hand = createHand(card());
        Card foundCard = hand.findCard(unboundJoker());
        assertNull(foundCard, "Null should be returned");
        
    }

    @Test
    public void testRemoveCard() {

        Card card = card();
        Hand hand = createHand(card);
        hand.removeCard(card);
        assertArrayEquals(emptyArray(), hand.getCards(), "Hand should be empty");

    }

    @Test
    public void testRemoveCardUsingSameCardRemovesCard() {

        Card card = card();
        Hand hand = createHand(card);
        hand.removeCard(copyOf(card));
        assertArrayEquals(emptyArray(), hand.getCards(), "Hand should be empty");

    }

    @Test
    public void testRemoveCardUsingBoundJokerRemovesCard() {

        Card card = card();
        Hand hand = createHand(card);
        hand.removeCard(boundJoker(copyOf(card)));
        assertArrayEquals(emptyArray(), hand.getCards(), "Hand should be empty");

    }

    @Test
    public void testRemoveCardUsingUnboundJokerRemovesJoker() {

        Joker joker = unboundJoker();
        Hand hand = createHand(joker);
        hand.removeCard(unboundJoker());
        assertArrayEquals(emptyArray(), hand.getCards(), "Hand should be empty");

    }

    @Test
    public void testRemoveCardFailsIfCardNotInHand() {

        Hand hand = createHand();
        assertThrows(IllegalArgumentException.class, () -> hand.removeCard(card()), "IllegalArgumentException should be thrown");
        
    }
}
