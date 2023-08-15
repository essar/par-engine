package uk.co.essarsoftware.par.game;

import java.util.Arrays;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.cards.PrialPlay;
import uk.co.essarsoftware.par.cards.RunPlay;

@Component
public class PlaySet extends HashMap<Player, Play[]>
{

    @Autowired
    private PlayerList players;

    public void initRound(final Round round) {

        // Remove any existing plays
        clear();

        // Create plays according to round
        switch (round) {
            case PP:
                players.forEach(player -> put(player, new Play[] {
                    new PrialPlay(), new PrialPlay()
                }));
                break;
            case PR:
                players.forEach(player -> put(player, new Play[] {
                    new PrialPlay(), new RunPlay()
                }));
                break;
            case RR:
                players.forEach(player -> put(player, new Play[] {
                    new RunPlay(), new RunPlay()
                }));
                break;
            case PPR:
                players.forEach(player -> put(player, new Play[] {
                    new PrialPlay(), new PrialPlay(), new RunPlay()
                }));
                break;
            case PRR:
                players.forEach(player -> put(player, new Play[] {
                    new PrialPlay(), new RunPlay(), new RunPlay()
                }));
                break;
            case PPP:
                players.forEach(player -> put(player, new Play[] {
                    new PrialPlay(), new PrialPlay(), new PrialPlay()
                }));
                break;
            case RRR:
                players.forEach(player -> put(player, new Play[] {
                    new RunPlay(), new RunPlay(), new RunPlay()
                }));
                break;
            case START:
            case END:
                // Create no plays
                break;
        }

    }

    public Play[] getAvailablePlays(Player player) {

        if (!containsKey(player)) {

            // No plays for this player
            return new Play[0];

        }

        return Arrays.stream(getPlayerPlays(player))
            .filter(p -> p.size() == 0)
            .toArray(Play[]::new);

    }

    public Play[] getPlayerPlays(Player player) {

        return get(player);

    }
}
