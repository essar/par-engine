package uk.co.essarsoftware.par.cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BoundJoker}.
 * @author essar
 */
public class BoundJokerTests
{

    @Test
    public void testNewBoundJokerFromNullFails() {

        assertThrows(IllegalArgumentException.class, () -> new BoundJoker(null), "Attempting to create bound joker from null should raise exception");

    }

    @Test
    public void testNewBoundJokerFromJokerFails() {

        assertThrows(IllegalArgumentException.class, () -> new BoundJoker(new UnboundJoker()), "Attempting to create bound joker from another joker should raise exception");
        
    }

    @Test
    public void testNewBoundJokerHasSuitFromCard() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertEquals(card.getSuit(), joker.getSuit(), "Bound joker should have suit from card");

    }

    @Test
    public void testNewBoundJokerHasValueFromCard() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertEquals(card.getValue(), joker.getValue(), "Bound joker should have value from card");

    }

    @Test
    public void testBindFails() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertThrows(IllegalStateException.class, () -> joker.bind(card), "Attempting to re-bind bound joker should raise exception");

    }

    @Test
    public void testNotEqualToNull() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertFalse(joker.equals(null), "Joker should not be equal to null");

    }

    @Test
    public void testJokerEqualToBoundCard() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertTrue(joker.equals(card), "Joker should equal bound card");

    }

    @Test
    public void testJokerNotEqualToDifferentCard() {

        Card card1 = Card.as(Suit.CLUBS, Value.KING);
        Card card2 = Card.as(Suit.SPADES, Value.ACE);
        BoundJoker joker = new BoundJoker(card1);
        assertFalse(joker.equals(card2), "Joker should not equal different card");

    }

    @Test
    public void testJokerNotEqualToUnboundJoker() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        Joker unboundJoker = new UnboundJoker(); 
        assertFalse(joker.equals(unboundJoker), "Joker should not equal unbound joker");

    }

    @Test
    public void testJokerEqualToJokerWithSameBoundCard() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker1 = new BoundJoker(card);
        BoundJoker joker2 = new BoundJoker(card);
        assertTrue(joker1.equals(joker2), "Joker should equal joker bound to same card");

    }

    @Test
    public void testGetCardIDIsNotNull() {

        BoundJoker joker = new BoundJoker(Card.as(Suit.CLUBS, Value.KING));
        assertNotNull(joker.getCardID(), "Card ID should not be null");

    }

    @Test
    public void testGetCardIDIsUnique() {

        BoundJoker joker1 = new BoundJoker(Card.as(Suit.CLUBS, Value.KING));
        BoundJoker joker2 = new BoundJoker(Card.as(Suit.CLUBS, Value.KING));
        assertNotEquals(joker1.getCardID(), joker2.getCardID(), "Card IDs of two jokers should be different");
    
    }

    @Test
    public void testIsBound() {

        BoundJoker joker = new BoundJoker(Card.as(Suit.CLUBS, Value.KING));
        assertTrue(joker.isBound(), "isBound should always be true");

    }

    @Test
    public void testIsJoker() {

        BoundJoker joker = new BoundJoker(Card.as(Suit.CLUBS, Value.KING));
        assertTrue(joker.isJoker(), "isJoker should always be true");

    }

    @Test
    public void testToString() {

        Card card = Card.as(Suit.CLUBS, Value.KING);
        BoundJoker joker = new BoundJoker(card);
        assertTrue(joker.toString().matches("BoundJoker\\[ID: [a-z0-9\\-]+; BoundCard: DefaultCard\\[ID: [a-z0-9\\-]+; Suit: CLUBS; Value: KING\\]\\]"), "Expected string to match pattern");
    
    }
}
