package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.game.Player;

public class PlayerPickupDiscardEvent extends EngineEvent
{

    private final Card card;
    private final Player player;

    public PlayerPickupDiscardEvent(Player player, Card card) {

        this.player = player;
        this.card = card;

    }

    public Card getCard() {

        return card;

    }

    public Player getPlayer() {

        return player;
        
    }
    
    @Override
    public String toString() {

        return String.format("[%s] %s picked up %s from the discard pile", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(card));

    }
}
