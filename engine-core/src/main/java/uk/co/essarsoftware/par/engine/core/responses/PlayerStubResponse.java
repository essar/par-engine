package uk.co.essarsoftware.par.engine.core.responses;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.players.Player;

public class PlayerStubResponse
{

    private final Player player;

    public PlayerStubResponse(Player player) {

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

    String toString(String format) {

        return String.format(format, getPlayerID(), getPlayerName(), getPlayerState());

    }

    @Override
    public String toString() {

        return toString("%s:  %s  [%s]");

    }
}
