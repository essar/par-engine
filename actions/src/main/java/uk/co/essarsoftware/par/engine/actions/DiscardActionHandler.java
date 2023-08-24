package uk.co.essarsoftware.par.engine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.engine.players.Player;

@Component
public class DiscardActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final DiscardPile discardPile;

    @Autowired
    public DiscardActionHandler(DiscardPile discardPile) {

        this.discardPile = discardPile;
        
    }

    public DiscardAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new DiscardAction(requestID, actionSequence, playerID);

    }

    public class DiscardAction extends Action<Card>
    {
        private DiscardAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        public Card getCard() {

            return getParameter("card", Card.class);
            
        }

        @Override
        public Card getResult() {
            
            return getParameter("discarded", Card.class);
            
        }

        public Card discard(Player currentPlayer, Card card) {

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

        @Override
        public void runAction(Player player) {

            discard(player, getParameter("card", Card.class));
            
        }
    }
}
