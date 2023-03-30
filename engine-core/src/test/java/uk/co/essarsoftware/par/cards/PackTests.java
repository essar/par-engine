package uk.co.essarsoftware.par.cards;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Pack}.
 * @author essar
 */
public class PackTests
{

    // TODO Reverse test args

    // Test utility methods

    private Card findCardFrom(Pack pack, Suit suit, Value value) {

        return pack.getCardStream().filter(c -> c.getSuit() == suit && c.getValue() == value).findFirst().get();

    }

    private Card getCardFrom(Pack pack) {

        return pack.getCardStream().findFirst().get();

    }

    private Joker getJokerFrom(Pack pack) {

        return (Joker) pack.getCardStream().filter(Card::isJoker).findFirst().get();

    }

    // Pack Tests

    @Test
    public void testGeneratePack() {

        Pack pack = Pack.generatePack();
        assertEquals(52, pack.getCards().length, "Pack should contain 52 cards");

        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.CLUBS).count(), "Pack should contain 13 clubs");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.DIAMONDS).count(), "Pack should contain 13 diamonds");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.HEARTS).count(), "Pack should contain 13 hearts");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.SPADES).count(), "Pack should contain 13 spades");

    }

    @Test
    public void testGeneratePackWithJokers() {

        Pack pack = Pack.generatePackWithJokers();
        assertEquals(54, pack.getCards().length, "Pack with jokers should contain 54 cards");

        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.CLUBS).count(), "Pack should contain 13 clubs");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.DIAMONDS).count(), "Pack should contain 13 diamonds");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.HEARTS).count(), "Pack should contain 13 hearts");
        assertEquals(13, pack.getCardStream().filter(c -> c.getSuit() == Suit.SPADES).count(), "Pack should contain 13 spades");

        assertEquals(2, pack.getCardStream().filter(Card::isJoker).count(), "Pack should contain 2 jokers");

    }

    // Pack.InternalCard Tests

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
        assertFalse(card.equals(Card.as(card.getSuit(), card.getValue())), "Card should not be equal non-pack card");

    }

    // Pack.InternalJoker Tests

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
