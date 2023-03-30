package uk.co.essarsoftware.par.cards;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DiscardPile}.
 * @author essar
 */
public class DiscardPileTests
{

    @Test
    public void testNewDiscardPileIsEmpty() {

        DiscardPile pile = new DiscardPile();
        assertEquals(pile.size(), 0, "New discard pile should have zero size");

    }

    @Test
    public void testDiscardIncreasesPileSize() {

        DiscardPile pile = new DiscardPile();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        int sizeBeforeDiscard = pile.size();
        pile.discard(card);
        assertEquals(pile.size(), sizeBeforeDiscard + 1, "Discarding a card should increase pile size");

    }

    @Test
    public void testDiscardPileContainsCardAfterDiscard() {

        DiscardPile pile = new DiscardPile();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        pile.discard(card);
        assertTrue(pile.getCardStream().anyMatch(card::equals), "Discard pile should contain discarded card");

    }

    @Test
    public void testDiscardNullRaisesException() {

        DiscardPile pile = new DiscardPile();
        assertThrows(IllegalArgumentException.class, () -> pile.discard(null), "Attempting to discard null should raise exception");

    }

    @Test
    public void testGetDiscardFromEmptyPileIsNull() {

        DiscardPile pile = new DiscardPile();
        assertNull(pile.getDiscard(), "Get discard from an empty pile should be return null");

    }

    @Test
    public void testGetDiscardReturnsLastDiscardedCard() {

        DiscardPile pile = new DiscardPile();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        pile.discard(card);
        assertEquals(pile.getDiscard(), card, "Get discard should return last discarded card");

    }

    @Test
    public void testPickupFromEmptyPileRaisesException() {

        DiscardPile pile = new DiscardPile();
        assertThrows(NoSuchElementException.class, () -> pile.pickup(), "Attempting to pickup from an empty pile should raise exception");

    }

    @Test
    public void testPickupReturnsLastDiscardedCard() {

        DiscardPile pile = new DiscardPile();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        pile.discard(card);
        assertEquals(pile.pickup(), card, "Pickup should return last discarded card");

    }

    @Test
    public void testPickupDecreasesPileSize() {

        DiscardPile pile = new DiscardPile();
        pile.discard(Card.as(Suit.CLUBS, Value.KING));
        int sizeBeforePickup = pile.size();
        pile.pickup();
        assertEquals(pile.size(), sizeBeforePickup - 1, "Pickup should decrease pile size");

    }



    @Test
    public void testDiscardPileNotContainsCardAfterPickup() {

        DiscardPile pile = new DiscardPile();
        pile.discard(Card.as(Suit.CLUBS, Value.KING));
        Card card = pile.pickup();
        assertFalse(pile.getCardStream().anyMatch(card::equals), "Discard pile should not contain card after pickup");

    }
  
}
