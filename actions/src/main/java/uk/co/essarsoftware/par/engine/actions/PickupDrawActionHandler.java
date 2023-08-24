package uk.co.essarsoftware.par.engine.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.players.Player;

@Component
public class PickupDrawActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final DrawPile drawPile;

    @Autowired
    public PickupDrawActionHandler(DrawPile drawPile) {

        this.drawPile = drawPile;

    }

    public PickupDrawAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new PickupDrawAction(requestID, actionSequence, playerID);

    }

    public class PickupDrawAction extends Action<Card>
    {

        private PickupDrawAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        @Override
        public Card getResult() {
            
            return getParameter("card", Card.class);
            
        }

        public Card pickupDraw(Player currentPlayer) {

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

        @Override
        public void runAction(Player player) {

            pickupDraw(player);

        }
    }
}
