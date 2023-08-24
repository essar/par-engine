package uk.co.essarsoftware.par.engine.core.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.engine.players.PlayersService;
import uk.co.essarsoftware.par.engine.plays.PlaySet;
import uk.co.essarsoftware.par.engine.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.game.Game;

import uk.co.essarsoftware.par.engine.game.Round;

@Service
public class GameServiceImpl
{

    private static final Logger _LOG = LoggerFactory.getLogger(GameServiceImpl.class);

    private final CardsService cards;
    private final EngineEventQueue eventQueue;
    private final Game game;
    private final PlaySet plays;
    private final PlayersService players;

    @Autowired
    public GameServiceImpl(Game game, EngineEventQueue eventQueue, PlayersService players, PlaySet plays, CardsService cards) {

        this.game = game;
        this.eventQueue = eventQueue;
        this.players = players;
        this.plays = plays;
        this.cards = cards;

    }

    private static Round getNextRound(Round currentRound) {

        switch (currentRound) {
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
        }
    }

    public Game getGame() {

        return game;

    }

    public synchronized StartRoundResponse startNextRound() {

        if (game.getCurrentRound() == Round.END) {

            throw new GameStateException("Game has already finished");

        }

        Round nextRound = getNextRound(game.getCurrentRound());
        if (nextRound == Round.END) {

            // Handle end of game activities
        }

        _LOG.info("Starting round {}", nextRound);

        // Check player states and move dealer token
        players.startNewRound();

        // Set next round
        game.setCurrentRound(nextRound);

        // Reinitialize card piles
        cards.initPiles();

        // Initialize plays
        plays.initRound(game.getCurrentRound());

        // Deal player hands
        cards.dealHands();

        // Set all players to WATCHING
        players.getPlayersStream().forEach(p -> players.setPlayerState(p, PlayerState.WATCHING));

        // Set current player to PICKUP
        //Player currentPlayer = players.getCurrentPlayer();
        //players.setPlayerState(currentPlayer, PlayerState.PICKUP);
        
        // Reset turn counter
        game.resetTurnCount();

        // Queue event
        eventQueue.queueEvent(new RoundStartedEvent(nextRound, players.getCurrentPlayer()));

        // Create response
        StartRoundResponse response = new StartRoundResponse(nextRound, players.getCurrentPlayer().getPlayerID());
            
        return response;

    }
}
