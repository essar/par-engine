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

@Component
public class PlayCardsActionHandler implements ActionHandler
{

    private static Logger _LOGGER = LoggerFactory.getLogger(PickupDrawActionHandler.class);

    private final PlaysService plays;

    @Autowired
    public PlayCardsActionHandler(PlaysService plays) {

        this.plays = plays;
        
    }

    public PlayCardsAction newAction(String requestID, Integer actionSequence, String playerID) {

        return new PlayCardsAction(requestID, actionSequence, playerID);

    }

    public class PlayCardsAction extends Action<Play>
    {
        private PlayCardsAction(String requestID, Integer actionSequence, String playerID) {

            super(requestID, actionSequence, playerID);

        }

        public Card[] getCards() {

            return getParameter("cards", Card[].class);

        }

        public Card[] getJokerBindings() {

            return getParameter("joker_bindings", Card[].class);
            
        }

        @Override
        public Play getResult() {
            
            return getParameter("play", Play.class);
            
        }

        public Play playCards(final Player currentPlayer, final Card[] cards) {

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

        @Override
        public void runAction(Player player) {

            playCards(player, getParameter("cards", Card[].class));
            
        }
    }
}
