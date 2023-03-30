package uk.co.essarsoftware.par.cards;

import static uk.co.essarsoftware.par.cards.TestCardFactory.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RunPlay}.
 * @author essar
 */
public class RunPlayTests
{

    @Test
    public void testNewPlayHasZeroSize() {

        RunPlay play = new RunPlay();
        assertEquals(0, play.size(), "New play should have zero size");

    }

    @Test
    public void testNewPlayIsEmpty() {

        RunPlay play = new RunPlay();
        assertArrayEquals(emptyArray(), play.getCards(), "New play should contain no cards");

    }

    @Test
    public void testAddUnboundJokerThrowsException() {

        RunPlay play = new RunPlay();
        assertThrows(IllegalArgumentException.class, () -> play.addCard(new UnboundJoker()), "Attempting to add an unbound joker should throw exception");

    }

    @Test
    public void testAddCardToEmptyPlay() {

        RunPlay play = new RunPlay();
        Card card = card();
        play.addCard(card);
        assertArrayEquals(asArray(card), play.getCards(), "Card should be added to the play");

    }

    @Test
    public void testAddCardIncrementsSize() {

        RunPlay play = new RunPlay();
        int sizeBeforeAdd = play.size();
        play.addCard(card());
        assertEquals(sizeBeforeAdd + 1, play.size(), "Play size should be incremented after add");

    }

    @Test
    public void testResetLeavesEmptyPlay() {

        RunPlay play = new RunPlay();
        play.addCard(card());
        play.reset();
        assertEquals(0, play.size(), "Play size after reset should be zero");

    }

    @Test
    public void testAddCardInRun() {

        RunPlay play = new RunPlay();
        Card[] cards = validRun();
        play.addCard(cards[0]);
        play.addCard(cards[1]);
        assertTrue(play.cards.contains(cards[1]), "Card should be present in play");

    }

    @Test
    public void testAddInvalidCardThrowsException() {

        RunPlay play = new RunPlay();
        Card[] cards = invalidRun();
        play.addCard(cards[0]);
        assertThrows(IllegalArgumentException.class, () -> play.addCard(cards[1]), "Attempting to add a card should throw exception");

    }

    @Test
    public void testSortRun() {

        RunPlay play = new RunPlay();
        Card[] cards = validRun();
        play.addCard(cards[2]);
        play.addCard(cards[1]);
        play.addCard(cards[0]);
        assertArrayEquals(cards, play.getCards(), "Cards should be sorted by suit and value");

    }

    @Test
    public void testGetAllowableCardsForEmptyRunIsAllCards() {

        RunPlay play = new RunPlay();
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(allCards(), cards, "Allowable cards should be all cards");

    }

    @Test
    public void testGetAllowableCardsForRunIsValuesEitherSide() {

        RunPlay play = new RunPlay();
        Card card = Card.as(Suit.CLUBS, Value.TEN);
        play.addCard(card);
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(asArray(card.getSuit(), Value.NINE, Value.JACK), cards, "Allowable cards should be cards on either side of the current run");

    }

    @Test
    public void testGetAllowableCardsForRunWithMultipleCardsIsValuesEitherSide() {

        RunPlay play = new RunPlay();
        Card card1 = Card.as(Suit.CLUBS, Value.TEN);
        Card card2 = Card.as(Suit.CLUBS, Value.JACK);
        play.addCard(card1);
        play.addCard(card2);
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(asArray(card1.getSuit(), Value.NINE, Value.QUEEN), cards, "Allowable cards should be cards on either side of the current run");

    }

    @Test
    public void testGetAllowableCardsForLowBoundedRunIsOnlyUpperCard() {

        RunPlay play = new RunPlay();
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        play.addCard(card);
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(asArray(Card.as(card.getSuit(), Value.TWO)), cards, "Allowable cards should just be upper card");

    }

    @Test
    public void testGetAllowableCardsForHighBoundedRunIsOnlyLowerCard() {

        RunPlay play = new RunPlay();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        play.addCard(card);
        Card[] cards = play.getAllowableCards();
        assertArrayEquals(asArray(Card.as(card.getSuit(), Value.QUEEN)), cards, "Allowable cards should just be upper card");

    }

    @Test
    public void testPlayIsNotPrial() {

        RunPlay play = new RunPlay();
        assertFalse(play.isPrial(), "Play should not be valid prial");

    }

    @Test
    public void testEmptyPlayIsNotRun() {

        RunPlay play = new RunPlay();
        assertFalse(play.isRun(), "Empty play should not be valid run");

    }

    @Test
    public void testTwoCardPlayIsNotRun() {

        RunPlay play = new RunPlay();
        Card[] cards = validRun();
        play.addCard(cards[0]);
        play.addCard(cards[1]);
        assertFalse(play.isRun(), "Play should not be valid run");

    }

    @Test
    public void testThreeCardPlayIsRun() {

        RunPlay play = new RunPlay();
        Card[] cards = validRun();
        play.addCard(cards[0]);
        play.addCard(cards[1]);
        play.addCard(cards[2]);
        assertTrue(play.isRun(), "Play should be valid run");

    }
}
