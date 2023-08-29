package uk.co.essarsoftware.par.cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultCard}.
 * @author essar
 */
public class DefaultCardTests
{

    @Test
    public void testCardEqualToSelf() {

        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertTrue(card.equals(card), "Card should equal itself");

    }

    @Test
    public void testCardEqualToSameCard() {

        Card card1 = new DefaultCard(Suit.CLUBS, Value.KING);
        Card card2 = new DefaultCard(card1.getSuit(), card1.getValue());
        assertTrue(card1.equals(card2), "Card should equal same card");

    }

    @Test
    public void testCardNotEqualToNull() {

        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertFalse(card.equals(null), "Card should not equal null");

    }

    @Test
    public void testCardNotEqualToDifferentSuit() {

        Card card1 = new DefaultCard(Suit.CLUBS, Value.KING);
        Card card2 = new DefaultCard(Suit.SPADES, card1.getValue());
        assertFalse(card1.equals(card2), "Card should not equal card with different Suit");

    }

    @Test
    public void testCardNotEqualToDifferentValue() {

        Card card1 = new DefaultCard(Suit.CLUBS, Value.KING);
        Card card2 = new DefaultCard(card1.getSuit(), Value.ACE);
        assertFalse(card1.equals(card2), "Card should not equal card with different Value");

    }

    @Test
    public void testCardNotEqualToUnboundJoker() {
        
        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        Joker joker = new UnboundJoker();
        assertFalse(card.equals(joker), "Card should not equal unbound joker");

    }

    @Test
    public void testCardEqualToBoundJokerBoundToSelf() {
        
        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        Joker joker = new BoundJoker(card);
        assertTrue(card.equals(joker), "Card should equal bound joker bound to self");

    }

    @Test
    public void testCardEqualToBoundJokerWithMatchingSuitAndValue() {
        
        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        Joker joker = new BoundJoker(new DefaultCard(card.getSuit(), card.getValue()));
        assertTrue(card.equals(joker), "Card should equal bound joker with matching suit and value");

    }

    @Test
    public void testCardNotEqualToBoundJokerWithDifferentSuit() {
        
        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        Joker joker = new BoundJoker(new DefaultCard(Suit.SPADES, card.getValue()));
        assertFalse(card.equals(joker), "Card should not equal bound joker with different suit");

    }

    @Test
    public void testCardNotEqualToBoundJokerWithDifferentValue() {
        
        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        Joker joker = new BoundJoker(new DefaultCard(card.getSuit(), Value.ACE));
        assertFalse(card.equals(joker), "Card should not equal bound joker with different suit");

    }

    @Test
    public void testGetCardIDIsNotNull() {

        DefaultCard card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertNotNull(card.getCardID(), "Card ID should not be null");

    }

    @Test
    public void testGetCardIDIsUnique() {

        DefaultCard card1 = new DefaultCard(Suit.CLUBS, Value.KING);
        DefaultCard card2 = new DefaultCard(Suit.CLUBS, Value.KING);
        assertNotEquals(card2.getCardID(), card1.getCardID(), "Card IDs of two cards should be different");
    
    }

    @Test
    public void testGetSuitNotNull() {

        DefaultCard card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertNotNull(card.getSuit(), "Card suit should not be null");

    }

    @Test
    public void testGetSuitReturnsSuit() {

        DefaultCard card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertEquals(Suit.CLUBS, card.getSuit(), "Expected card to have CLUBS suit");

    }

    @Test
    public void testGetValueReturnsValue() {

        DefaultCard card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertEquals(Value.KING, card.getValue(), "Expected card to have KING value");

    }

    @Test
    public void testIsJokerReturnsFalse() {

        DefaultCard card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertFalse(card.isJoker(), "Expected isJoker to be false");
        
    }

    @Test
    public void testToString() {

        Card card = new DefaultCard(Suit.CLUBS, Value.KING);
        assertTrue(card.toString().matches("DefaultCard@[a-z0-9\\-]+\\[suit=CLUBS,value=KING\\]"), "Expected string to match pattern");
    
    }
    
}
