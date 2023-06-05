package uk.co.essarsoftware.par.engine.core.app.players;

import java.util.stream.Stream;

import uk.co.essarsoftware.par.game.Player;

public class GetPlayersResponse implements PlayersResponse
{
    private final Stream<Player> players;

    public GetPlayersResponse(Stream<Player> players) {

        this.players = players;

    }

    public Stream<PlayerStubResponse> getPlayers() {

        return players.map(PlayerStubResponse::new);

    }

    @Override
    public String toString() {

        String[] allPlayers = players.map(Player::toString).map(s -> String.format("  --> %s", s)).toArray(String[]::new);
        return String.format("%d Player(s)%n%s", allPlayers.length, String.join("\n", allPlayers));

    }
}
