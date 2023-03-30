package uk.co.essarsoftware.par.cards;

/**
 * Represents a penalty card held by a player. May be set to hold a given card, or cleared. Only one penalty
 * card may be held at any time and it must be explicitly cleared before another one can be set.
 */
public class PenaltyCard {
    
    private Card penaltyCard;

    /**
     * Instantiate a new PenaltyCard object.
     */
    public PenaltyCard() {

        // Empty constructor

    }

    /**
     * Clears the existing penalty card.
     */
    public void clearPenaltyCard() {

        penaltyCard = null;

    }

    /**
     * Gets the current penalty card.
     * @return the currently held penalty card, or {@code null} if no penalty card is held.
     */
    public Card getPenaltyCard() {

        return penaltyCard;

    }

    /**
     * Indicates if a penalty card is currently held.
     * @return {@code true} if a penalty card is currently being held.
     */
    public boolean hasPenaltyCard() {

        return penaltyCard != null;

    }

    /**
     * Sets the penalty card.
     * @param penaltyCard the Card to set as the penalty card.
     * @throws IllegalArgumentException if {@code card} is {@code null}; use {@link #clearPenaltyCard()} instead.
     * @throws IllegalStateException if a penalty card is already being held.
     */
    public void setPenaltyCard(Card penaltyCard) {

        if (penaltyCard == null) {

            throw new IllegalArgumentException("Cannot set penalty card to null");

        }
        if (this.penaltyCard != null) {

            throw new IllegalStateException("Penalty card is already set");

        }

        this.penaltyCard = penaltyCard;

    }
}
