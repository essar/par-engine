package uk.co.essarsoftware.par.engine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Action handler class for {@link PickupDrawAction}.
 * Picks up a card from the draw pile and adds it to the player's hand.
 * @author @essar
 */
@Component
public class PickupDrawActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final DrawPile drawPile;

    /**
     * Instantiate a new PickupDrawActionHandler.
     * @param discardPile The game draw pile.
     */
    public PickupDrawActionHandler(final DrawPile drawPile) {

        this.drawPile = drawPile;

    }

    /**
     * Create a new {@link PickupDrawAction}.
     * @param requestID parent request ID.
     * @param actionSequence current action sequence number.
     * @param playerID ownering player ID.
     * @return a new PickupDrawAction object.
     */
    public PickupDrawAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new PickupDrawAction(requestID, actionSequence, playerID);

    }

    /**
     * {@link Action} subclass that picks up a card from the draw pile and adds it to a player's hand.
     * @author @essar
     */
    public class PickupDrawAction extends Action<Card>
    {

        private PickupDrawAction(String requestID, Integer actionSequence, String playerID) {

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
         * Pickup from the draw pile.
         * @param currentPlayer the player to perform the pickup.
         * @return the card picked up.
         */
        public Card pickupDraw(Player currentPlayer) {

            if (currentPlayer == null) {

                throw new IllegalArgumentException("Current player cannot be null");
            
            }

            // Take card from draw pile
            final Card card = drawPile.pickup();
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
        public void runAction(Player player) {

            pickupDraw(player);

        }
    }
}
