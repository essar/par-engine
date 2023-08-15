package uk.co.essarsoftware.par.engine.core.app.players;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.game.Player;

public class GetPlayersResponse implements PlayersResponse
{
    private final Stream<Player> players;

    public GetPlayersResponse(Stream<Player> players) {

        this.players = players;

    }

    public Stream<PlayerListResponse> getPlayers() {

        return players.map(PlayerListResponse::new);

    }

    @Override
    public String toString() {

        String[] allPlayers = players.map(Player::toString).map(s -> String.format("  --> %s", s)).toArray(String[]::new);
        return String.format("%d Player(s)%n%s", allPlayers.length, String.join("\n", allPlayers));

    }

    static class PlayerListResponse
    {

        private Player player;

        PlayerListResponse(Player player) {

            this.player = player;

        }

        @JsonGetter("player_id")
        public String getPlayerID() {

            return player.getPlayerID();

        }

        @JsonGetter("player_name")
        public String getPlayerName() {

            return player.getPlayerName();

        }

        @JsonGetter("player_state")
        public String getPlayerState() {

            return player.getPlayerState().name();

        }
    }
}
