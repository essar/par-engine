package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
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
        assertEquals(0, pile.size(), "New draw pile should have zero size");

    }

    @Test
    public void testClearPileContainsNoCards() {

        DrawPile pile = new DrawPile();
        pile.addPack(Pack.generatePack());
        pile.clear();
        assertArrayEquals(TestCardFactory.emptyArray(), pile.getCardStream().toArray(Card[]::new), "Cleared pile should contain no cards");

    }

    @Test
    public void testClearBecomesZeroSize() {

        DrawPile pile = new DrawPile();
        pile.addPack(Pack.generatePack());
        pile.clear();
        assertEquals(0, pile.getCardStream().count(), "Cleared pile should have zero size");

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
        assertEquals(card, pile.pickup(), "Pickup should return first card in pack");

    }

    @Test
    public void testDrawPileSizeDecreasesAfterPickup() {

        DrawPile pile = new DrawPile();
        Pack pack = Pack.generatePack();
        pile.addPack(pack);
        int sizeBeforePickup = pile.size();
        pile.pickup();
        assertEquals(sizeBeforePickup - 1, pile.size(), "Draw pile size should decrease by 1 after pickup");

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
        assertEquals(0, pile.size(), "Shuffled empty pile should still have zero length");

    }

    @Test
    public void testShufflePileDoesNotChangeSize() {

        DrawPile pile = new DrawPile();
        pile.addPack(Pack.generatePack());
        int sizeBeforeShuffle = pile.size();
        pile.shuffle();
        assertEquals(sizeBeforeShuffle, pile.size(), "Draw pile shuffle should not change pile size");

    }

    @Test
    public void testShufflePileWithRandomSeedChangesResult() {

        DrawPile pile1 = new DrawPile();
        DrawPile pile2 = new DrawPile();
        Pack pack = Pack.generatePack();
        pile1.addPack(pack);
        pile2.addPack(pack);
        pile2.shuffle();
        assertFalse(Arrays.equals(pile1.getCardStream().toArray(Card[]::new), pile2.getCardStream().toArray(Card[]::new)), "Shuffled piles should be different to unshuffled pile");
    
    }

    @Test
    public void testShufflePileWithDifferentSeedChangesResult() {

        DrawPile pile1 = new DrawPile();
        DrawPile pile2 = new DrawPile();
        Pack pack = Pack.generatePack();
        pile1.addPack(pack);
        pile2.addPack(pack);
        pile1.shuffle(1L);
        pile2.shuffle(2L);
        assertFalse(Arrays.equals(pile1.getCardStream().toArray(Card[]::new), pile2.getCardStream().toArray(Card[]::new)), "Piles shuffled with different seeds should have different result");
    
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
