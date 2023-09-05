package uk.co.essarsoftware.par.engine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Action handler class for {@link PickupDiscardAction}.
 * Picks up a card from the discard pile and adds it to the player's hand.
 * @author @essar
 */
@Component
public class PickupDiscardActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final DiscardPile discardPile;

    /**
     * Instantiate a new PickupDiscardActionHandler.
     * @param discardPile The game discard pile.
     */
    @Autowired
    public PickupDiscardActionHandler(final DiscardPile discardPile) {

        this.discardPile = discardPile;
        
    }

    /**
     * Create a new {@link PickupDiscardAction}.
     * @param requestID parent request ID.
     * @param actionSequence current action sequence number.
     * @param playerID ownering player ID.
     * @return a new PickupDiscardAction object.
     */
    public PickupDiscardAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new PickupDiscardAction(requestID, actionSequence, playerID);

    }

    /**
     * {@link Action} subclass that picks up a card from the discard pile and adds it to a player's hand.
     * @author @essar
     */
    public class PickupDiscardAction extends Action<Card>
    {
        private PickupDiscardAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        /**
         * @see Action#getResult()
         */
        @Override
        public Card getResult() {
            
            return getParameter("card", Card.class);
            
        }

        /**
         * Pickup from the discard pile.
         * @param currentPlayer the player to perform the pickup.
         * @return the card picked up.
         */
        public Card pickupDiscard(final Player currentPlayer) {

            if (currentPlayer == null) {

                throw new IllegalArgumentException("Current player cannot be null");
            
            }

            // Take card from draw pile
            final Card card = discardPile.pickup();
            _LOGGER.debug("Picked up {}", card);
            
            // Add card to player hand
            currentPlayer.getHand().addCard(card);
            _LOGGER.debug("Card added to player hand; size: {} card(s)", currentPlayer.getHandSize());

            // Set result
            setParameter("card", card);
            
            return card;

        }

        /**
         * @see Action#runAction(Player).
         */
        @Override
        public void runAction(final Player player) {

            pickupDiscard(player);

        }
    }
}
