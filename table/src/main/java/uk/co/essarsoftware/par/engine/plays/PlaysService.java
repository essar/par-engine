package uk.co.essarsoftware.par.engine.plays;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.players.Player;

public interface PlaysService
{

    public Play buildPlayForPlayer(final Player player, final Card[] cards);

    public boolean hasAvailablePlaysRemaining(Player player);

}
