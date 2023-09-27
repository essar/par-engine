package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

/**
 * {@link EngineEvent} raised when a player changes state.
 * @author @essar
 */
public class PlayerStateChangeEvent extends EngineEvent
{

    private final Player player;
    private final PlayerState oldState, newState;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} whose state has changed.
     * @param oldState the previous {@link PlayerState}.
     * @param newState the new {@link PlayerState}.
     */
    public PlayerStateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {

        this.player = player;
        this.oldState = oldState;
        this.newState = newState;

    }

    /**
     * Get the player's new state.
     * @return the new {@link PlayerState}.
     */
    public PlayerState getNewState() {

        return newState;

    }

    /**
     * Get the player's old state.
     * @return the previous {@link PlayerState}.
     */
    public PlayerState getOldState() {

        return oldState;
        
    }

    /**
     * Get the player.
     * @return the {@link Player} whose state has changed.
     */
    public Player getPlayer() {
        
        return player;

    }

    /**
     * @see Object#toString()
     */
    public String toString() {

        return String.format("[%s] %s state changed: %s -> %s", getClass().getSimpleName(), player.getPlayerName(), oldState, newState);

    }
}
