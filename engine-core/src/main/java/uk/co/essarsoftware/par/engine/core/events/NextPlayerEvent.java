package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

public class NextPlayerEvent extends EngineEvent
{
    private final Player player;

    public NextPlayerEvent(Player player) {

        this.player = player;

    }

    public Player getPlayer() {

        return player;

    }
}
