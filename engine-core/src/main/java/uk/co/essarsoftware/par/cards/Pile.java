package uk.co.essarsoftware.par.cards;

import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Represents an ordered stack of Cards that is generally accessed at one end only.
 * @author essar
 */
abstract class Pile
{

    protected final LinkedList<Card> cards;

    /**
     * Instantiates a new empty Pile.
     */
    protected Pile() {

        cards = new LinkedList<>();

    }

    /**
     * Gets a CardContainer instance providing low-level access to the cards in this Play.
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
     * Gets all the Cards currently in the Pile.
     * @return a Stream object containing all the cards in the Pile.
     */
    Stream<Card> getCardStream() {

        return cards.stream();
        
    }

    /**
     * Gets a count of the current number of cards in the Pile.
     * @return the count as an integer.
     */
    int size() {

        return cards.size();

    }

    /**
     * Removes all cards from the Pile.
     */
    public void clear() {

        synchronized (cards) {

            cards.clear();

        }
    }

    /**
     * Retrieves the top card in the Pile and removes it from the Pile.
     * @return the top Card.
     */
    public Card pickup() {

        synchronized (cards) {

            return cards.removeFirst();

        }
    }
}
