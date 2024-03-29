package uk.co.essarsoftware.par.engine.core.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.engine.core.app.CardsService;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.players.PlayersService;

/**
 * Task executed upon starting a new round. May execute asynchronously.
 * @author @essar
 */
@Component
public class StartRoundTask
{

    private static final Logger _LOG = LoggerFactory.getLogger(StartRoundTask.class);

    private CardsService cards;
    private PlayersService players;

    /**
     * Instantiate this task with service beans.
     * @param cards service exposing card functions.
     * @param players service exposing player fuinctions.
     */
    public StartRoundTask(CardsService cards, PlayersService players) {

        this.cards = cards;
        this.players = players;

    }

    /**
     * Process a received {@link RoundStartedEvent}.
     * @param event the event to process.
     */
    @Async
    public void processEvent(RoundStartedEvent event) {

        _LOG.info("Starting round {}", event.getRound());

        // Reinitialize card piles
        cards.initPiles();

        // Deal player hands
        cards.dealHands();

        // Set all players to WATCHING
        players.getPlayersStream().forEach(p -> p.setPlayerState(PlayerState.WATCHING));

        // Set current player to PICKUP
        Player currentPlayer = players.getCurrentPlayer();
        currentPlayer.setPlayerState(PlayerState.PICKUP);
        
    }
}
