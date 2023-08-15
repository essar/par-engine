package uk.co.essarsoftware.par.engine.core.app.players;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.game.Player;

@ResponseStatus(code = HttpStatus.CREATED)
public class CreatePlayerResponse
{
    private final Player player;

    public CreatePlayerResponse(Player player) {

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

    public String asTable() {

        return String.format("%10S | %30S | %10S", "PLAYER ID", "PLAYER NAME", "STATE")
            + String.format("%n-----------+--------------------------------+------------")
            + String.format("%n%10s | %30s | %10s", getPlayerID(), getPlayerName(), getPlayerState());

    }

    @Override
    public String toString() {

        return String.format("Created Player %s", player.getPlayerID());

    }
}
