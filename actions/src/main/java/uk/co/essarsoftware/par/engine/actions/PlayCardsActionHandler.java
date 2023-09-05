package uk.co.essarsoftware.par.engine.actions;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.plays.PlaysService;

/**
 * Action handler class for {@link PlayCardsAction}.
 * Takes three cards from the player's hand and tries to build them into a Play on the table.
 * @author @essar
 */
@Component
public class PlayCardsActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final PlaysService plays;

    /**
     * Instantiate a new PlayCardsActionHandler.
     * @param plays Service exposing play methods on the game.
     */
    @Autowired
    public PlayCardsActionHandler(final PlaysService plays) {

        this.plays = plays;
        
    }

    /**
     * Create a new {@link PlayCardsAction}.
     * @param requestID parent request ID.
     * @param actionSequence current action sequence number.
     * @param playerID ownering player ID.
     * @return a new PlayCardsAction object.
     */
    public PlayCardsAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new PlayCardsAction(requestID, actionSequence, playerID);

    }

    /**
     * {@link Action} subclass that takes three cards from the player's hand and tries to build them into a Play on the table.
     * @author @essar
     */
    public class PlayCardsAction extends Action<Play>
    {
        private PlayCardsAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        public Card[] getCards() {

            return getParameter("cards", Card[].class);

        }

        /* public Card[] getJokerBindings() {

            return getParameter("joker_bindings", Card[].class);
            
        } */

        /**
         * @see Action#getResult()
         */
        @Override
        public Play getResult() {
            
            return getParameter("play", Play.class);
            
        }

        /**
         * Attempt to make a play from the cards.
         * @param currentPlayer the player to perform the pickup.
         * @param cards an array of {@link Card}s to try and build into a Play.
         * @return the Play created.
         */
        public Play playCards(final Player currentPlayer, final Card[] cards) {

            if (currentPlayer == null) {

                throw new IllegalArgumentException("Current player cannot be null");
            }

            if (cards == null) {

                throw new IllegalArgumentException("Card array cannot be null");
            }

            // Try and find an available play
            Play play = plays.buildPlayForPlayer(currentPlayer, cards);
            _LOGGER.debug("Built Play from cards: {}", play);

            // Remove cards from player hand
            Arrays.stream(cards).forEach(currentPlayer.getHand()::removeCard);
            _LOGGER.debug("Cards removed from player hand; size: {} card(s)", currentPlayer.getHandSize());

            // Set result
            setParameter("play", play);
            
            return play;

        }

        /**
         * @see Action#runAction(Player).
         */
        @Override
        public void runAction(Player player) {

            playCards(player, getCards());
            
        }
    }
}
