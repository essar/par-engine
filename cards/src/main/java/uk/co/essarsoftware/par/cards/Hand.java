package uk.co.essarsoftware.par.cards;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Player's hand of cards.
 * @author essar
 */
public class Hand
{
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(Hand.class);

    private final ArrayList<Card> cards;

    /**
     * Instantiate a new empty Hand.
     */
    public Hand() {

        cards = new ArrayList<>();

    }

    /**
     * Add a card to the Hand
     * @param card the card to add to the hand.
     * @throws IllegalArgumentException if {@code card} already exists in the hand.
     */
    public void addCard(Card card) {

        synchronized (cards) {

            _LOGGER.debug("addCard({})", card);
            if (cards.contains(card)) {

                throw new IllegalArgumentException("Card already in hand");

            }
            cards.add(card);
            _LOGGER.debug("Added card: {}", card);

        }
    }
    
    /**
     * Removes all cards from the Hand.
     */
    public void clear() {

        synchronized (cards) {

            _LOGGER.trace("clear()");
            cards.clear();
            _LOGGER.debug("Hand cleared");

        }
    }

    /**
     * Finds a Card instance in the hand that matches the Suit and Value of the supplied card.
     * @param card a Card to find in the Hand.
     * @return a Card instance from the hand, or {@code null} if no card can be found.
     */
    public Card findCard(Card card) {

        _LOGGER.trace("findCard({})", card);
        try {
        
            Card foundCard = null;

            if (card.isJoker() && !((Joker) card).isBound()) {

                // Find an unbound joker from the Hand
                foundCard = cards.stream()
                    .filter(
                        c -> c.isJoker()
                        && !((Joker) c).isBound()
                    )
                    .findAny().get();

            } else {

                foundCard = cards.stream()
                    .filter(
                        c -> c.getSuit() == card.getSuit()
                        && c.getValue() == card.getValue()
                    )
                    .findAny().get();
            
            }
            _LOGGER.debug("findCard: {} -> {}", card, foundCard);
            return foundCard;

        } catch (NoSuchElementException e) {

            _LOGGER.debug("findCard({}): No match", card);
            return null;

        }
    }

    /**
     * Gets all cards in the hand.
     * @return an array of Cards.
     */
    public Card[] getCards() {

        return cards.toArray(Card[]::new);
        
    }

    /**
     * Gets a stream containing all cards in the hand.
     * @return an Stream of Cards.
     */
    public Stream<Card> getCardsStream() {

        return cards.stream();
        
    }

    /**
     * Discard a card from the hand. The card is automatically resolved to a matching card instance in the hand.
     * @param card the Card to discard.
     * @throws IllegalArgumentException if {@code card} is not present in the Hand.
     * @see #findCard(Card)
     */
    public void removeCard(Card card) {

        _LOGGER.trace("removeCard({})", card);
        synchronized (cards) {

            Card resolvedCard = findCard(card);
            if (resolvedCard == null) {

                throw new IllegalArgumentException("Card not contained in hand");

            }
            cards.remove(resolvedCard);
            _LOGGER.debug("Removed card: {}", resolvedCard);

        }
    }

    /**
     * Gets the size of the Hand.
     * @return the number of cards in the hand.
     */
    public int size() {

        return cards.size();

    }
}
