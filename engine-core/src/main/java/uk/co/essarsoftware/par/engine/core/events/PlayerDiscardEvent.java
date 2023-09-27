package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * {@link EngineEvent} raised when a player discards a card.
 * @author @essar
 */
public class PlayerDiscardEvent extends EngineEvent
{

    private final Card card;
    private final Player player;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} discarding the card.
     * @param card the {@link Card} being discarded.
     */
    public PlayerDiscardEvent(Player player, Card card) {

        this.player = player;
        this.card = card;

    }

    /**
     * Get the discarded card.
     * @return the {@link Card} being discarded.
     */
    public Card getCard() {

        return card;

    }

    /**
     * Get the player.
     * @return the {@link Player} discarding the card.
     */
    public Player getPlayer() {

        return player;
        
    }
    
    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("[%s] %s discarded %s", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(card));

    }
}
