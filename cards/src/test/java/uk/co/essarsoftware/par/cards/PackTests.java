package uk.co.essarsoftware.par.cards;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.cards.Pack.PackCard;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Pack}.
 * @author @essar
 */
public class PackTests
{

    // Test utility methods

    private Card findCardFrom(Pack pack, Suit suit, Value value) {

        return pack.getCardStream().filter(c -> c.getSuit() == suit && c.getValue() == value).findFirst().get();

    }

    private Card getCardFrom(Pack pack) {

        return pack.getCardStream().dropWhile(Card::isJoker).findFirst().get();

    }

    private Joker getJokerFrom(Pack pack) {

        return (Joker) pack.getCardStream().filter(Card::isJoker).findFirst().get();

    }

    // Pack Tests

    @Test
    public void testAddDuplicateEntryThrowsException() {

        Pack pack = Pack.generatePack();
        PackCard card = (PackCard) getCardFrom(pack);
        assertThrows(IllegalArgumentException.class, () -> pack.addEntry(card), "Expected exception to be thrown attempting to add duplicate card");

    }
    
    @Test
    public void testAddEntryFromDifferentPackThrowsException() {

        Pack pack1 = Pack.generatePack();
        Pack pack2 = Pack.generatePack();
        PackCard card = (PackCard) getCardFrom(pack2);
        assertThrows(IllegalArgumentException.class, () -> pack1.addEntry(card), "Expected exception to be thrown attempting to add card from antother pack");
        
    }

    @Test
    public void testGeneratePackContains52Cards() {

        Pack pack = Pack.generatePack();
        assertEquals(52, pack.getCards().length, "Pack should contain 52 cards");

    }

    @Test
    public void testGeneratePackContains13OfEachSuit() {

        Pack pack = Pack.generatePack();
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.CLUBS).distinct().count(), "Pack should contain 13 clubs");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.DIAMONDS).distinct().count(), "Pack should contain 13 diamonds");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.HEARTS).distinct().count(), "Pack should contain 13 hearts");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.SPADES).distinct().count(), "Pack should contain 13 spades");

    }

    @Test
    public void testGeneratePackWithJokers() {

        Pack pack = Pack.generatePackWithJokers();
        assertEquals(54, pack.getCards().length, "Pack with jokers should contain 54 cards");

    }

    @Test
    public void testGeneratePackWithJokersContains2Jokers() {

        Pack pack = Pack.generatePackWithJokers();

        assertEquals(2, pack.getCardStream().filter(Card::isJoker).distinct().count(), "Pack should contain 2 jokers");

    }

    // Pack.PackCard Tests

    @Test
    public void testPackCardEqualToSelf() {

        Pack pack = Pack.generatePack();
        Card card = getCardFrom(pack);
        assertTrue(card.equals(card), "Card should equal itself");

    }

    @Test
    public void testPackCardNotEqualToCardInDifferentPack() {

        Pack pack1 = Pack.generatePack();
        Pack pack2 = Pack.generatePack();
        Card card1 = getCardFrom(pack1);
        Card card2 = findCardFrom(pack2, card1.getSuit(), card1.getValue());
        assertFalse(card1.equals(card2), "Cards from different packs should not be equal");
        assertFalse(card2.equals(card1), "Cards from different packs should not be equal");
    
    }

    @Test
    public void testPackCardNotEqualNull() {

        Pack pack = Pack.generatePack();
        Card card = getCardFrom(pack);
        assertFalse(card.equals(null), "Card should not be equal to null");

    }

    @Test
    public void testPackCardNotEqualToNonPackCard() {

        Pack pack = Pack.generatePack();
        Card card = getCardFrom(pack);
        Card card2 = TestCardFactory.card(card.getSuit(), card.getValue());
        assertFalse(card.equals(card2), "Card should not be equal non-pack card");

    }

    @Test
    public void testPackCardToStringReturnsExpectedString() {

        Pack pack = Pack.generatePack();
        Card card = findCardFrom(pack, Suit.CLUBS, Value.ACE);
        assertTrue(card.toString().matches("PackCard@[a-z0-9\\-]+\\[packId=[a-z0-9\\-]+,suit=CLUBS,value=ACE\\]"), "Expected string to match pattern");

    }

    // Pack.PackUnboundJoker Tests

    @Test
    public void testPackJokerEqualToSelf() {

        Pack pack = Pack.generatePackWithJokers();
        Joker joker = getJokerFrom(pack);
        assertEquals(joker, joker, "Joker should equal itself");

    }

    @Test
    public void testPackJokerCanBeBoundToPackCard() {

        Pack pack = Pack.generatePackWithJokers();
        Card card = getCardFrom(pack);
        Joker joker = getJokerFrom(pack);
        Joker boundJoker = joker.bind(card);
        assertTrue(boundJoker.isBound(), "Joker can be bound to pack card");

    }

    @Test
    public void testPackJokerCanNotBeBoundToNonPackCard() {

        Pack pack = Pack.generatePackWithJokers();
        Card card = Card.as(Suit.CLUBS, Value.KING);
        Joker joker = getJokerFrom(pack);
        assertThrows(IllegalArgumentException.class, () -> joker.bind(card), "Joker should not bind to non-pack card");
        
    }

    // Pack.PackBoundJoker Tests

    @Test
    public void testPackBoundJokerEqualsPackCard() {

        Pack pack = Pack.generatePackWithJokers();
        Card card = getCardFrom(pack);
        Joker joker = getJokerFrom(pack);
        Joker boundJoker = joker.bind(card);
        assertTrue(boundJoker.equals(card), "Bound joker should equal pack card");

    }

    @Test
    public void testPackCardEqualsPackBoundJoker() {

        Pack pack = Pack.generatePackWithJokers();
        Card card = getCardFrom(pack);
        Joker joker = getJokerFrom(pack);
        Joker boundJoker = joker.bind(card);
        assertTrue(card.equals(boundJoker), "Pack card should equal bound joker");

    }

    @Test
    public void testPackBoundJokerNotEqualToNonPackCard() {

        Pack pack = Pack.generatePackWithJokers();
        Card card = getCardFrom(pack);
        Card nonPackCard = Card.as(card.getSuit(), card.getValue());
        Joker joker = getJokerFrom(pack);
        Joker boundJoker = joker.bind(card);
        assertFalse(boundJoker.equals(nonPackCard), "Bound joker should not equal not-pack card");
        
    }
}
