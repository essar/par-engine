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
    
}
