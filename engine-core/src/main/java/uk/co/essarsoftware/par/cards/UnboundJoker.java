package uk.co.essarsoftware.par.cards;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of a Joker that has not been bound to a card.
 */
class UnboundJoker implements Joker
{

    private final UUID cardID;
    
    /**
     * Instantiates a new UnboundJoker.
     */
    UnboundJoker() {

        cardID = UUID.randomUUID();

    }

    /**
     * Creates a new BoundJoker bound to the specified card.
     * @param card the card to bind to the joker.
     * @return a new BoundJoker object.
     */
    protected BoundJoker createBoundJoker(Card card) {

        return new BoundJoker(card);

    }

    /**
     * Gets the card ID.
     * @return a globally unique identifier for this UnboundJoker instance.
     */
    protected String getCardID() {

        return cardID.toString();

    }

    /**
     * @see Joker#bind(Card)
     */
    @Override
    public Joker bind(Card card) {

        BoundJoker boundJoker = createBoundJoker(card);
        return boundJoker;

    }

    /**
     * Tests if this UnboundJoker is equal to another card.
     * UnboundJokers are never equal to any other card.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {

        if (o instanceof UnboundJoker) {

            // Unbound jokers never equal another joker, so only test on instance ID
            return Objects.equals(cardID, ((UnboundJoker) o).cardID);

        }

        return false;

    }

    /**
     * @return always returns {@code false}.
     * @see Joker#isBound()
     */
    @Override
    public boolean isBound() {

        return false;

    }

    /**
     * @return always returns {@code true}.
     * @see Joker#isJoker()
     */
    @Override
    public final boolean isJoker() {

        return true;

    }

    /**
     * @return always returns {@code null}.
     * @see Joker#getSuit()
     */
    @Override
    public Suit getSuit() {

        return null;

    }

    /**
     * @return alays returns {@code null}.
     * @see Joker#getValue()
     */
    @Override
    public Value getValue() {

        return null;

    }    

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        
        return String.format("UnboundJoker[ID: %s]", cardID);

    }
}
