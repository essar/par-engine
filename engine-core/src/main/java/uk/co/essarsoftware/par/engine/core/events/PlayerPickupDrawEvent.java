package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

public class PlayerPickupDrawEvent extends EngineEvent
{

    private final Player player;

    public PlayerPickupDrawEvent(Player player) {

        this.player = player;

    }

    public Player getPlayer() {

        return player;
        
    }
    
    @Override
    public String toString() {

        return String.format("[%s] %s picked up from the draw pile", getClass().getSimpleName(), player.getPlayerName());

    }
}
