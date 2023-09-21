package uk.co.essarsoftware.par.engine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Action handler class for {@link DiscardAction}.
 * Discards a card from a player's hand, adding it to the discard pile.
 * @author @essar
 */
@Component
public class DiscardActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final DiscardPile discardPile;

    /**
     * Instantiate a new DiscardActionHandler.
     * @param discardPile The game discard pile.
     */
    public DiscardActionHandler(final DiscardPile discardPile) {

        this.discardPile = discardPile;
        
    }

    /**
     * Create a new {@link DiscardAction}.
     * @param requestID parent request ID.
     * @param actionSequence current action sequence number.
     * @param playerID ownering player ID.
     * @return a new DiscardAction object.
     */
    public DiscardAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new DiscardAction(requestID, actionSequence, playerID);

    }

    /**
     * {@link Action} subclass that discards a card from a player's hand, adding it to the discard pile.
     * Expected request parameters:
     * - {@code card} a {@link Card} to discard.
     * @author @essar
     */
    public class DiscardAction extends Action<Card>
    {
        /**
         * Private class constructor, new instances should be obtained using {@link DiscardActionHandler#newAction(String, Integer, String)}.
         * @param requestID parent request ID.
         * @param actionSequence current action sequence number.
         * @param playerID ownering player ID.
         */
        private DiscardAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        /**
         * Get the card to discard.
         * @return the Card that should be discarded.
         */
        public Card getCard() {

            return getParameter("card", Card.class);
            
        }

        /**
         * @see Action#getResult()
         */
        @Override
        public Card getResult() {
            
            return getParameter("discarded", Card.class);
            
        }

        /**
         * Discard a card from the players hand.
         * @param currentPlayer the player to perform the discard.
         * @param card the card to discard.
         * @return the actual card object discarded.
         */
        public Card discard(final Player currentPlayer, final Card card) {

            if (currentPlayer == null) {

                throw new IllegalArgumentException("Current player cannot be null");
            }
            if (card == null) {

                throw new IllegalArgumentException("Card cannot be null");

            }

            // Remove card from player hand
            currentPlayer.getHand().removeCard(card);
            _LOGGER.debug("Card removed from player hand; size: {} card(s)", currentPlayer.getHandSize());

            // Add card to discard pile
            discardPile.discard(card);
            _LOGGER.debug("Discarded {}", card);

            // Set result
            setParameter("discarded", card);
            
            return card;

        }

        /**
         * @see Action#runAction(Player).
         */
        @Override
        public void runAction(final Player player) {

            discard(player, getCard());
            
        }
    }
}
