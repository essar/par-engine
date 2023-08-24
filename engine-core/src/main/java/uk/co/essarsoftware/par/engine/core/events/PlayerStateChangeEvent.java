package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

public class PlayerStateChangeEvent extends EngineEvent
{

    private final Player player;
    private final PlayerState oldState, newState;

    public PlayerStateChangeEvent(Player player, PlayerState oldState, PlayerState newState) {

        this.player = player;
        this.oldState = oldState;
        this.newState = newState;

    }

    public PlayerState getNewState() {

        return newState;

    }

    public PlayerState getOldState() {

        return oldState;
        
    }

    public Player getPlayer() {
        
        return player;

    }

    public String toString() {

        return String.format("[%s] %s state changed: %s -> %s", getClass().getSimpleName(), player.getPlayerName(), oldState, newState);

    }
}
