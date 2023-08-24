package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

public class PlayerPlayCardsEvent extends EngineEvent
{

    private final Player player;
    private final Play play;

    public PlayerPlayCardsEvent(Player player, Play play) {

        this.player = player;
        this.play = play;

    }

    public Play getPlay() {

        return play;

    }

    public Player getPlayer() {

        return player;
        
    }
    
    @Override
    public String toString() {

        return String.format("[%s] %s played %s", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(play.getCards()));

    }
}
