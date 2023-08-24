package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class containing numerous static helper methods for creating Cards and sets of Cards.
 * @author essar
 */
class CardFactory
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(Hand.class);

    /**
     * Creates a new Stream containing all standard cards.
     * @return a Stream containing the standard 52 cards.
     */
    private static Stream<Card> cardStream() {

        return Arrays.stream(Suit.values())
            .flatMap(s -> Arrays.stream(Value.values())
                .map(v -> generateCard(s, v)));

    }
    
    /**
     * Generates a new card of given suit and value.
     * @param suit the card suit.
     * @param value the card face value.
     * @return a new Card implementation.
     */
    protected static Card generateCard(Suit suit, Value value) {

        return Card.as(suit, value);

    }

    /**
     * Gets all cards.
     * @return an array containing all cards.
     */
    static Card[] allCards() {

        Card[] cards = cardStream().toArray(Card[]::new);

        _LOGGER.debug("Generated {} card(s)", cards.length);
        return cards;

    }

    /**
     * Gets all cards in a given suit.
     * @param suit the card suit.
     * @return an array containing all cards for the specified suit.
     */
    static Card[] forSuit(Suit suit) {

        Card[] cards = cardStream()
            .filter(c -> c.getSuit() == suit)
            .toArray(Card[]::new);
        
        _LOGGER.debug("Generated {} card(s) for suit {}", cards.length, suit);
        return cards;

    }

    /**
     * Gets all cards with a given value.
     * @param value the card face value.
     * @return an array containing all cards with the specified value.
     */
    static Card[] forValue(Value value) {

        Card[] cards = cardStream()
            .filter(c -> c.getValue() == value)
            .toArray(Card[]::new);

        _LOGGER.debug("Generated {} card(s) for value {}", cards.length, value);
        return cards;

    }

    /**
     * Gets a card with the previous value to an existing card.
     * @param card a card.
     * @return a new Card with the same suit but previous value to the specified card, or {@code null} if the specified card has no previous card.
     */
    static Card withPreviousValue(Card card) {

        try {

            Value nextValue = Value.values()[card.getValue().ordinal() - 1];
            return Card.as(card.getSuit(), nextValue);

        } catch (ArrayIndexOutOfBoundsException e) {

            _LOGGER.debug("Unable to find a card previous to {}, returning null", card);
            return null;
            
        }
    }

    /**
     * Gets a card with the next value to an existing card.
     * @param card a card.
     * @return a new Card with the same suit but next value to the specifed card, or {@code null} if the specified card has no next value.
     */
    static Card withNextValue(Card card) {

        try {

            Value nextValue = Value.values()[card.getValue().ordinal() + 1];
            return Card.as(card.getSuit(), nextValue);

        } catch (ArrayIndexOutOfBoundsException e) {

            _LOGGER.debug("Unable to find a card next to {}, returning null", card);
            return null;

        }
    }
}
