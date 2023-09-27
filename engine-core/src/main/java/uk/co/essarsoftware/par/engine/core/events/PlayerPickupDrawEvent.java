package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * {@link EngineEvent} raised when a player picks up a card from the draw pile.
 * @author @essar
 */
public class PlayerPickupDrawEvent extends EngineEvent
{

    private final Player player;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} picking up the card.
     */
    public PlayerPickupDrawEvent(Player player) {

        this.player = player;

    }

    /**
     * Get the player.
     * @return the {@link Player} picking up the card.
     */
    public Player getPlayer() {

        return player;
        
    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("[%s] %s picked up from the draw pile", getClass().getSimpleName(), player.getPlayerName());

    }
}
