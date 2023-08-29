package uk.co.essarsoftware.par.cards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a penalty card held by a player. May be set to hold a given card, or cleared. Only one penalty
 * card may be held at any time and it must be explicitly cleared before another one can be set.
 */
public class PenaltyCard
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(PenaltyCard.class);
    
    private Card penaltyCard;

    /**
     * Instantiate a new PenaltyCard object.
     */
    public PenaltyCard() {

        // Empty constructor

    }

    /**
     * Clear the existing penalty card.
     */
    public void clearPenaltyCard() {

        penaltyCard = null;

    }

    /**
     * Get the current penalty card.
     * @return the currently held penalty card, or {@code null} if no penalty card is held.
     */
    public Card getPenaltyCard() {

        return penaltyCard;

    }

    /**
     * Indicate if a penalty card is currently held.
     * @return {@code true} if a penalty card is currently being held.
     */
    public boolean hasPenaltyCard() {

        return penaltyCard != null;

    }

    /**
     * Set the penalty card.
     * @param penaltyCard the Card to set as the penalty card.
     * @throws IllegalArgumentException if {@code card} is {@code null}; use {@link #clearPenaltyCard()} instead.
     * @throws IllegalStateException if a penalty card is already being held.
     */
    public void setPenaltyCard(Card penaltyCard) {

        _LOGGER.trace("setPenaltyCard({})", penaltyCard);

        if (penaltyCard == null) {

            throw new IllegalArgumentException("Cannot set penalty card to null");

        }
        if (this.penaltyCard != null) {

            throw new IllegalStateException("Penalty card is already set");

        }

        this.penaltyCard = penaltyCard;
        _LOGGER.debug("Penalty card set: {}", penaltyCard);

    }
}
