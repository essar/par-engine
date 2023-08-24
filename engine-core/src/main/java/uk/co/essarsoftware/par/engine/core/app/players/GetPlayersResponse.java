package uk.co.essarsoftware.par.engine.core.app.players;

import java.util.stream.Stream;

import uk.co.essarsoftware.par.engine.players.Player;

public class GetPlayersResponse
{
    private final Stream<Player> players;

    private static String formatPlayer(PlayerStubResponse player) {

        return player.toString("%8s: %-30s [%10s]");

    }

    public GetPlayersResponse(Stream<Player> players) {

        this.players = players;

    }

    public Stream<PlayerStubResponse> getPlayers() {

        return players.map(PlayerStubResponse::new);

    }

    @Override
    public String toString() {

        String[] allPlayers = getPlayers().map(GetPlayersResponse::formatPlayer).map(s -> String.format("  --> %s", s)).toArray(String[]::new);
        return String.format("%d Player(s)%n%s", allPlayers.length, String.join("\n", allPlayers));

    }
}
