package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.game.Round;

/**
 * {@link EngineEvent} raised when a new round is started.
 * @author @essar
 */
public class RoundStartedEvent extends EngineEvent
{

    private final Player currentPlayer;
    private final Round round;

    /**
     * Instantiate a new event.
     * @param round the {@link Round} that has started.
     * @param player the {@link Player} whose turn it now is.
     */
    public RoundStartedEvent(Round round, Player currentPlayer) {

        this.round = round;
        this.currentPlayer = currentPlayer;

    }

    /**
     * Get current player.
     * @return the {@link Player} whose turn it now is.
     */
    public Player getCurrentPlayer() {

        return currentPlayer;
        
    }

    /**
     * Get round.
     * @return the {@link Round} that has started.
     */
    public Round getRound() {

        return round;

    }

    /**
     * @see Object#toString()
     */
    public String toString() {

        return String.format("[%s] Round %s started; %s is the first player", getClass().getSimpleName(), round, currentPlayer.getPlayerName());

    }
}
