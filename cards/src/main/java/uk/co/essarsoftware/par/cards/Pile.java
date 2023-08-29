package uk.co.essarsoftware.par.cards;

import java.util.LinkedList;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an ordered stack of Cards that is generally accessed at one end only.
 * @author essar
 */
abstract class Pile
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(Pile.class);

    protected final LinkedList<Card> cards;

    /**
     * Instantiate a new empty Pile.
     */
    protected Pile() {

        cards = new LinkedList<>();

    }

    /**
     * Get a CardContainer instance providing low-level access to the cards in this Play.
     * @return a CardContainer referencing all the cards in this play.
     */
    CardContainer getCardContainer() {

        return new CardContainer() {
            
            @Override
            public Stream<Card> getCardStream() {
                
                return cards.stream();

            }
        };
    }

    /**
     * Get all the Cards currently in the Pile.
     * @return a Stream object containing all the cards in the Pile.
     */
    Stream<Card> getCardStream() {

        return cards.stream();
        
    }

    /**
     * Get a count of the current number of cards in the Pile.
     * @return the count as an integer.
     */
    int size() {

        return cards.size();

    }

    /**
     * Remove all cards from the Pile.
     */
    public void clear() {

        _LOGGER.trace("clear()");

        synchronized (cards) {

            cards.clear();
            _LOGGER.debug("Pile cleared");

        }
    }

    /**
     * Retrieve the top card in the Pile and remove it from the Pile.
     * @return the top Card.
     */
    public Card pickup() {

        synchronized (cards) {

            _LOGGER.trace("pickup()");
            return cards.removeFirst();

        }
    }
}
