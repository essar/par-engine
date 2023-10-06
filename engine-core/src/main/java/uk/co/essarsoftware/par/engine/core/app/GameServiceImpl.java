package uk.co.essarsoftware.par.engine.core.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.engine.players.PlayersService;
import uk.co.essarsoftware.par.engine.plays.PlaysService;
import uk.co.essarsoftware.par.engine.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.core.exceptions.GameStateException;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.GameService;
import uk.co.essarsoftware.par.engine.game.Round;

/**
 * Implementation of {@link GameService}, providing standard game process.
 * @author @essar
 */
@Service
public class GameServiceImpl implements GameService
{

    private static final Logger _LOG = LoggerFactory.getLogger(GameServiceImpl.class);

    private final CardsService cards;
    private final EngineEventQueue eventQueue;
    private final Game game;
    private final PlaysService plays;
    private final PlayersService players;

    private static final Map<Round, Round> NEXT_ROUND = Map.of(
            Round.START, Round.PP,
            Round.PP, Round.PR,
            Round.PR, Round.RR,
            Round.RR, Round.PPR,
            Round.PPR, Round.PRR,
            Round.PRR, Round.PPP,
            Round.PPP, Round.RRR,
            Round.RRR, Round.END,
            Round.END, Round.END);

    public GameServiceImpl(Game game, EngineEventQueue eventQueue, PlayersService players, PlaysService plays, CardsService cards) {

        this.game = game;
        this.eventQueue = eventQueue;
        this.players = players;
        this.plays = plays;
        this.cards = cards;

    }

    private static Round getNextRound(Round currentRound) {

        return NEXT_ROUND.get(currentRound);

        /* switch (currentRound) {
            case START: return Round.PP;
            case PP: return Round.PR;
            case PR: return Round.RR;
            case RR: return Round.PPR;
            case PPR: return Round.PRR;
            case PRR: return Round.PPP;
            case PPP: return Round.RRR;
            case RRR: return Round.END;
            case END: return Round.END;
            default: throw new IllegalArgumentException(String.format("Unexpected round: %s", currentRound));
        } */
    }

    @Override
    public Game getGame() {

        return game;

    }

    /**
     * @see {@link GameService#startNextRound()}.
     */
    @Override
    public synchronized Round startNextRound() {

        if (game.getCurrentRound() == Round.END) {

            throw new GameStateException("Game has already finished");

        }

        Round nextRound = getNextRound(game.getCurrentRound());
        if (nextRound == Round.END) {

            // Handle end of game activities
            _LOG.info("Game ended");

            // Stop here
            return nextRound;
            
        }

        _LOG.debug("Starting round {}", nextRound);

        // Check player states and move dealer token
        players.startNewRound();

        // Set next round
        game.setCurrentRound(nextRound);

        // Reinitialize card piles
        cards.initPiles();

        // Initialize plays
        plays.initPlaysForRound(nextRound);

        // Deal player hands
        cards.dealHands();

        // Set all players to WATCHING
        players.getPlayersStream().forEach(p -> players.setPlayerState(p, PlayerState.WATCHING));

        // Reset turn counter
        game.resetTurnCount();

        // Queue event
        eventQueue.queueEvent(new RoundStartedEvent(nextRound, players.getCurrentPlayer()));

        return nextRound;

    }
}
