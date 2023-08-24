package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.game.Round;

public class RoundStartedEvent extends EngineEvent
{

    private final Player currentPlayer;
    private final Round round;

    public RoundStartedEvent(Round round, Player currentPlayer) {

        this.round = round;
        this.currentPlayer = currentPlayer;

    }

    public Player getCurrentPlayer() {

        return currentPlayer;
        
    }

    public Round getRound() {

        return round;

    }

    public String toString() {

        return String.format("[%s] Round %s started; %s is the first player", getClass().getSimpleName(), round, currentPlayer.getPlayerName());

    }
}
