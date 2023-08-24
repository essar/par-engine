package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class representing a set of played cards; nominally a Prial or a Run.
 */
public abstract class Play implements Comparator<Card>
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(Pile.class);
    
    protected final TreeSet<Card> cards;

    /**
     * Instantiate a new empty Play.
     */
    protected Play() {

        cards = new TreeSet<>(this);

    }

    /**
     * Gets an array that indicates the valid cards able to be played in this play.
     * @return an array containing Cards that can be added to this play.
     */
    public abstract Card[] getAllowableCards();

    /**
     * Tests if this is a valid Prial.
     * @return {@code true} if this play contains at least 3 cards of a matching Value; {@code false} otherwise.
     */
    public abstract boolean isPrial();

    /**
     * Tests if this is a valid Run.
     * @return {@code true} if this play contains at least 3 cards of a matching Suit and sequential Values; {@code false} otherwise.
     */
    public abstract boolean isRun();

    /**
     * Adds a card to this play.
     * @param card the Card to add.
     * @throws IllegalArgumentException if {@code card} cannot be added to the Play.
     */
    public void addCard(Card card) {

        _LOGGER.trace("addCard({})", card);

        if (card.isJoker() && !((Joker) card).isBound()) {

            throw new IllegalArgumentException("Cannot add unbound joker to play");

        }

        synchronized (cards) {

            if (cards.contains(card)) {

                throw new IllegalArgumentException("Cannot add duplicate card to play");

            }

            if (!Arrays.stream(getAllowableCards()).anyMatch(c -> c.equals(card))) {

                throw new IllegalArgumentException("Cannot add card to play");

            }
            cards.add(card);
            _LOGGER.debug("Added card: {}", card);

        }
    }

    /**
     * Gets all the cards currently in this Play.
     * @return an array containing the Cards.
     */
    public Card[] getCards() {

        return cards.toArray(Card[]::new);

    }

    /**
     * Resets the current Play and removes all cards.
     */
    public void reset() {

        _LOGGER.trace("reset()");

        synchronized (cards) {

            cards.clear();
            _LOGGER.debug("Play reset");

        }
    }

    /**
     * Counts the number of cards in the Play.
     * @return the number of cards in the Play.
     */
    public int size() {

        return cards.size();

    }
}
