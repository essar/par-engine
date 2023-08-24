package uk.co.essarsoftware.par.engine.core.events;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.players.Player;

public class PlayerDiscardEvent extends EngineEvent
{

    private final Card card;
    private final Player player;

    public PlayerDiscardEvent(Player player, Card card) {

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

        return String.format("[%s] %s discarded %s", getClass().getSimpleName(), player.getPlayerName(), CardEncoder.asShortString(card));

    }
}
