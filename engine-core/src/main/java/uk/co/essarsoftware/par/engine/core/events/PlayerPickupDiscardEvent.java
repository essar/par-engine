package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * {@link EngineEvent} raised when a player picks up a card from the discard pile.
 * @author @essar
 */
public class PlayerPickupDiscardEvent extends EngineEvent
{

    private final Card card;
    private final Player player;

    /**
     * Instantiate a new event.
     * @param player the {@link Player} picking up the card.
     * @param card the {@link Card} picked up.
     */
    public PlayerPickupDiscardEvent(Player player, Card card) {

        this.player = player;
        this.card = card;

    }

    /**
     * Get the pickup card.
     * @return the {@link Card} picked up.
     */
    public Card getCard() {

        return card;

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

        return String.format("[%s] %s picked up %s from the discard pile", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(card));

    }
}
