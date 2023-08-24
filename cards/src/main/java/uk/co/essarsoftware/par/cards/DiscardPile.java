package uk.co.essarsoftware.par.cards;

import org.springframework.stereotype.Component;

/**
 * Represents a pile of discarded cards, which can be added to (discard) or remove from (pickup).
 * @author essar
 */
@Component
public class DiscardPile extends Pile
{

    /**
     * Add a discarded card to the DiscardPile.
     * @param card the Card to discard.
     * @throws IllegalArgumentExeption if a {@code null} card is discarded.
     */
    public void discard(Card card) {

        if (card == null) {

            throw new IllegalArgumentException("Cannot discard null");

        }

        synchronized (cards) {
        
            cards.addFirst(card);

        }
    }

    /**
     * Gets the top card in the DiscardPile.
     * @return the top Card in the pile.
     */
    public Card getDiscard() {

        Card discard;
        synchronized (cards) {

            discard = cards.isEmpty() ? null : cards.getFirst();

        }
        return discard;
    }
}
