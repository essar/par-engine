package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * {@link EngineEvent} raised when a player plays cards to the table.
 * @author @essar
 */
public class PlayerPlayCardsEvent extends EngineEvent
{

    private final Player player;
    private final Play play;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} making the play.
     * @param play the {@link Play} being played.
     */
    public PlayerPlayCardsEvent(Player player, Play play) {

        this.player = player;
        this.play = play;

    }

    /**
     * Get the play.
     * @return the {@link Play} being played.
     */
    public Play getPlay() {

        return play;

    }

    /**
     * Get the player.
     * @return the {@link Player} making the play.
     */
    public Player getPlayer() {

        return player;
        
    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("[%s] %s played %s", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(play.getCards()));

    }
}
