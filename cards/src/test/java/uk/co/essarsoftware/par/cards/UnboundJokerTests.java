package uk.co.essarsoftware.par.cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link UnboundJoker}.
 * @author essar
 */
public class UnboundJokerTests
{

    @Test
    public void testBind() {

        UnboundJoker joker = new UnboundJoker();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        assertNotNull(joker.bind(card), "BoundJoker should be returned");

    }

    @Test
    public void testGetCardIDIsNotNull() {

        UnboundJoker joker = new UnboundJoker();
        assertNotNull(joker.getCardID(), "Card ID should not be null");

    }

    @Test
    public void testGetCardIDIsUnique() {

        UnboundJoker joker1 = new UnboundJoker();
        UnboundJoker joker2 = new UnboundJoker();
        assertNotEquals(joker1.getCardID(), joker2.getCardID(), "Card IDs of two jokers should be different");
    
    }

    @Test
    public void testGetSuit() {

        UnboundJoker joker = new UnboundJoker();
        assertNull(joker.getSuit(), "getSuit should always be null");

    }

    @Test
    public void testGetValue() {

        UnboundJoker joker = new UnboundJoker();
        assertNull(joker.getValue(), "getValue should always be null");

    }

    @Test
    public void testIsBound() {

        UnboundJoker joker = new UnboundJoker();
        assertFalse(joker.isBound(), "isBound should always be false");

    }

    @Test
    public void testIsJoker() {

        UnboundJoker joker = new UnboundJoker();
        assertTrue(joker.isJoker(), "isJoker should always be true");

    }

    @Test
    public void testIsEqualToSelf() {

        UnboundJoker joker = new UnboundJoker();
        assertTrue(joker.equals(joker), "UnboundJoker should equal self");

    }

    @Test
    public void testIsNotEqualToNull() {

        UnboundJoker joker = new UnboundJoker();
        assertFalse(joker.equals(null), "UnboundJoker should not equal null");

    }

    @Test
    public void testIsNotEqualToAnotherUnboundJoker() {

        UnboundJoker joker1 = new UnboundJoker();
        UnboundJoker joker2 = new UnboundJoker();
        assertFalse(joker1.equals(joker2), "UnboundJoker should not equal another UnboundJoker");

    }

    @Test
    public void testToString() {

        UnboundJoker joker = new UnboundJoker();
        assertTrue(joker.toString().matches("UnboundJoker\\[ID: [a-z0-9\\-]+\\]"), "Expected string to match pattern");
    
    }
    
}
