package uk.co.essarsoftware.par.engine.plays;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Set of functions that can be performed against {@link Play} objects in the game.
 * @author @essar
 */
public interface PlaysService
{

    /**
     * Attempt to build a play, owned by a {@link Player}, using a set of provided {@link Card}s.
     * @param player the owner of the Play.
     * @param cards an array of Cards to build the Play from.
     * @return the Play that has been built.
     */
    public Play buildPlayForPlayer(final Player player, final Card[] cards);

    /**
     * Checks if a {@link Player} has available plays remaining on the table.
     * @param player the owner of the Plays.
     * @return {@code true} if the player has nore plays, {@code false} otherwise.
     */
    public boolean hasAvailablePlaysRemaining(Player player);

    /**
     * Initialise plays for the specified round.
     * @param round the current game round.
     */
    public void initPlaysForRound(final Round round);

}
