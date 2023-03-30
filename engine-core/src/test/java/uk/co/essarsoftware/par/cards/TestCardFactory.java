package uk.co.essarsoftware.par.cards;

import java.util.Arrays;

/**
 * Test utility class for creating Cards.
 * @author essar
 */
class TestCardFactory
{

    static Card[] allCards() {

        return CardFactory.allCards();

    }

    static Card altCard() {

        return Card.as(Suit.SPADES, Value.ACE);

    }

    static Card[] asArray(Card... cards) {

        return cards;

    }

    static Card[] asArray(Suit suit, Value... values) {

        return Arrays.stream(values)
            .map(v -> Card.as(suit, v))
            .toArray(Card[]::new);

    }

    static Joker boundJoker(Card card) {

        return new UnboundJoker().bind(card);

    }

    static Card card() {

        return Card.as(Suit.CLUBS, Value.KING);

    }

    static Card card(Suit suit, Value value) {

        return Card.as(suit, value);

    }

    static Card copyOf(Card card) {

        return Card.as(card.getSuit(), card.getValue());
        
    }

    static Card[] forSuit(Suit suit) {

        return CardFactory.forSuit(suit);

    }

    static Card[] emptyArray() {

        return new Card[0];
    }

    static Card[] forValue(Value value) {

        return CardFactory.forValue(value);
    
    }

    static Card[] invalidPrial() {

        return new Card[] {
            Card.as(Suit.CLUBS, Value.KING),
            Card.as(Suit.SPADES, Value.ACE)
        };

    }

    static Card[] invalidRun() {

        return new Card[] {
            Card.as(Suit.CLUBS, Value.TEN),
            Card.as(Suit.SPADES, Value.ACE)
        };

    }

    static Card[] validPrial() {

        Value prialValue = Value.KING;
        return new Card[] {
            Card.as(Suit.CLUBS, prialValue),
            Card.as(Suit.DIAMONDS, prialValue),
            Card.as(Suit.SPADES, prialValue)
        };

    }

    static UnboundJoker unboundJoker() {

        return new UnboundJoker();
        
    }

    static Card[] validRun() {

        Suit runSuit = Suit.CLUBS;
        return new Card[] {
            Card.as(runSuit, Value.TEN),
            Card.as(runSuit, Value.JACK),
            Card.as(runSuit, Value.QUEEN)
        };

    }
}
