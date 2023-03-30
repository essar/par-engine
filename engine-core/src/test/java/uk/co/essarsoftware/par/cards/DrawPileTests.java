package uk.co.essarsoftware.par.cards;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DrawPile}.
 * @author essar
 */
public class DrawPileTests
{

    @Test
    public void testNewDrawPileIsEmpty() {

        DrawPile pile = new DrawPile();
        assertEquals(pile.size(), 0, "New draw pile should have zero size");

    }

    @Test
    public void testPickupFromEmptyPileRaisesException() {

        DrawPile pile = new DrawPile();
        assertThrows(NoSuchElementException.class, () -> pile.pickup(), "Attempting to pickup from an empty pile should raise exception");

    }

    @Test
    public void testPickupReturnsFirstCardInPack() {

        DrawPile pile = new DrawPile();
        Pack pack = Pack.generatePack();
        pile.addPack(pack);
        Card card = pack.getCardStream().findFirst().get();
        assertEquals(pile.pickup(), card, "Pickup should return first card in pack");

    }

    @Test
    public void testDrawPileSizeDecreasesAfterPickup() {

        DrawPile pile = new DrawPile();
        Pack pack = Pack.generatePack();
        pile.addPack(pack);
        int sizeBeforePickup = pile.size();
        pile.pickup();
        assertEquals(pile.size(), sizeBeforePickup - 1, "Draw pile size should decrease by 1 after pickup");

    }

    @Test
    public void testDrawPileNotContainsCardAfterPickup() {

        DrawPile pile = new DrawPile();
        Pack pack = Pack.generatePack();
        pile.addPack(pack);
        Card card = pile.pickup();
        assertFalse(pile.getCardStream().anyMatch(card::equals), "Draw pile should not contain card after pickup");

    }

    @Test
    public void testShuffleEmptyDrawPile() {

        DrawPile pile = new DrawPile();
        pile.shuffle();
        assertEquals(pile.size(), 0 , "Shuffled empty pile should still have zero length");

    }

    @Test
    public void testShufflePileDoesNotChangeSize() {

        DrawPile pile = new DrawPile();
        pile.addPack(Pack.generatePack());
        int sizeBeforeShuffle = pile.size();
        pile.shuffle();
        assertEquals(pile.size(), sizeBeforeShuffle, "Draw pile shuffle should not change pile size");

    }

    @Test
    public void testShufflePileWithSameSeedHasSameResult() {

        DrawPile pile1 = new DrawPile();
        DrawPile pile2 = new DrawPile();
        Pack pack = Pack.generatePack();
        long seed = 0L;
        pile1.addPack(pack);
        pile2.addPack(pack);
        pile1.shuffle(seed);
        pile2.shuffle(seed);
        assertArrayEquals(pile1.getCardStream().toArray(Card[]::new), pile2.getCardStream().toArray(Card[]::new), "Piles shuffled with same seed should have same result");
    
    }
}
