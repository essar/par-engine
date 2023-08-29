package uk.co.essarsoftware.par.engine.plays;

import java.util.Arrays;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Holds the {@code Play}s that are part of the game table.
 * Plays are retrievable by their owner, a {@link Player}.
 * @author @essar
 */
@Component
public class PlaySet extends HashMap<Player, Play[]>
{

    /**
     * Get available plays owned by a given player. Available plays
     * are ones that have not yet been built with cards.
     * @param player the owner of the plays to search.
     * @return an array of available Plays.
     */
    public Play[] getAvailablePlays(Player player) {

        Play[] plays;
        synchronized (this) {
            if (!containsKey(player)) {

                // No plays for this player
                plays = new Play[0];

            } else {

                plays = Arrays.stream(getPlayerPlays(player))
                    .filter(p -> p.size() == 0)
                    .toArray(Play[]::new);

            }
        }
        return plays;
    }

    /**
     * Get all {@link Play}s owned by a {@link Player}.
     * @param player the owner of the plays to search.
     * @return an array of all owned Plays.
     */
    public Play[] getPlayerPlays(Player player) {

        return get(player);

    }
}
