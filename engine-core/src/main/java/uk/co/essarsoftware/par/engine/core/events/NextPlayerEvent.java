package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * {@link EngineEvent} raised when play passes to the next player.
 * @author @essar
 */
public class NextPlayerEvent extends EngineEvent
{
    private final Player player;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} whose turn it now is.
     */
    public NextPlayerEvent(Player player) {

        this.player = player;

    }

    /**
     * Get the next player.
     * @return the {@link Player} whose turn it now is.
     */
    public Player getPlayer() {

        return player;

    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("[%s] %s now the active player", getClass().getSimpleName(), player.getPlayerName());

    }
}
