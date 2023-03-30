package uk.co.essarsoftware.par.cards;

import static uk.co.essarsoftware.par.cards.TestCardFactory.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link PrialPlay}.
 * @author essar
 */
public class PrialPlayTests
{

    @Test
    public void testNewPlayHasZeroSize() {

        PrialPlay play = new PrialPlay();
        assertEquals(0, play.size(), "New play should have zero size");

    }

    @Test
    public void testNewPlayIsEmpty() {

        PrialPlay play = new PrialPlay();
        assertArrayEquals(emptyArray(), play.getCards(), "New play should contain no cards");

    }

    @Test
    public void testAddUnboundJokerRaisesException() {

        PrialPlay play = new PrialPlay();
        assertThrows(IllegalArgumentException.class, () -> play.addCard(new UnboundJoker()), "Attempting to add an unbound joker to a Play should fail");

    }

    @Test
    public void testAddCardToEmptyPlay() {

        PrialPlay play = new PrialPlay();
        Card card = card();
        play.addCard(card);
        assertArrayEquals(asArray(card), play.getCards(), "Card should be added to the play");

    }

    @Test
    public void testAddCardIncrementsSize() {

        PrialPlay play = new PrialPlay();
        int sizeBeforeAdd = play.size();
        play.addCard(card());
        assertEquals(sizeBeforeAdd + 1, play.size(), "Play size should be incremented after add");

    }

    @Test
    public void testAddSameCardThrowsException() {

        PrialPlay play = new PrialPlay();
        Card card = card();
        play.addCard(card);
        assertThrows(IllegalArgumentException.class, () -> play.addCard(copyOf(card)), "Attempting to add duplicate card should throw exception");
        
    }

    @Test
    public void testAddBoundJoker() {

        PrialPlay play = new PrialPlay();
        Card[] cards = validPrial();
        play.addCard(cards[0]);
        play.addCard(cards[1]);
        Joker joker = boundJoker(cards[2]);
        play.addCard(joker);
        assertArrayEquals(cards, play.getCards(), "Play should contain bound joker");

    }

    @Test
    public void testAddBoundJokerNotInAllowableCardsRaisesException() {

        PrialPlay play = new PrialPlay();
        Card[] cards = invalidPrial();
        play.addCard(cards[0]);
        Joker joker = boundJoker(cards[1]);
        assertThrows(IllegalArgumentException.class, () -> play.addCard(joker), "Attempting to add a card not in allowable cards should fail");

    }

    @Test
    public void testResetLeavesEmptyPlay() {

        PrialPlay play = new PrialPlay();
        play.addCard(card());
        play.reset();
        assertEquals(0, play.size(), "Play size after reset should be zero");

    }

    @Test
    public void testAddCardInPrial() {

        PrialPlay play = new PrialPlay();
        Card[] cards = validPrial();
        play.addCard(cards[0]);
        play.addCard(cards[1]);
        assertTrue(play.cards.contains(cards[1]), "Card should be present in play");

    }

    @Test
    public void testAddInvalidCardThrowsException() {

        PrialPlay play = new PrialPlay();
        Card[] prialCards = invalidPrial();
        play.addCard(prialCards[0]);
        assertThrows(IllegalArgumentException.class, () -> play.addCard(prialCards[1]), "Attempting to add a card should throw exception");

    }

    @Test
    public void testSortPrial() {

        PrialPlay play = new PrialPlay();
        Card[] cards = validPrial();
        play.addCard(cards[1]);
        play.addCard(cards[2]);
        play.addCard(cards[0]);
        assertArrayEquals(cards, play.getCards(), "Cards should be sorted by suit");

    }

    @Test
    public void testGetAllowableCardsForEmptyPrialIsAllCards() {

        PrialPlay play = new PrialPlay();
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(allCards(), cards, "Allowable cards for an empty play should be all cards");

    }

    @Test
    public void testGetAllowableCardsForPrialIsCardsSameValue() {

        PrialPlay play = new PrialPlay();
        Card card = card();
        play.addCard(card);
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(forValue(card.getValue()), cards, "Allowable cards for a play with a card should be all cards of same value");

    }

    @Test
    public void testEmptyPlayIsNotPrial() {

        PrialPlay play = new PrialPlay();
        assertFalse(play.isPrial(), "Empty play should not be valid prial");

    }

    @Test
    public void testTwoCardPlayIsNotPrial() {

        PrialPlay play = new PrialPlay();
        Card[] prialCards = validPrial();
        play.addCard(prialCards[0]);
        play.addCard(prialCards[1]);
        assertFalse(play.isPrial(), "Play should not be valid prial");

    }

    @Test
    public void testThreeCardPlayIsPrial() {

        PrialPlay play = new PrialPlay();
        Card[] prialCards = validPrial();
        play.addCard(prialCards[0]);
        play.addCard(prialCards[1]);
        play.addCard(prialCards[2]);
        assertTrue(play.isPrial(), "Play should be valid prial");

    }

    @Test
    public void testPlayIsNotRun() {

        PrialPlay play = new PrialPlay();
        assertFalse(play.isRun(), "Play should not be valid run");

    }
}
