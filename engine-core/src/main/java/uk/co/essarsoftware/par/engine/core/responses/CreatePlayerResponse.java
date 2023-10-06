package uk.co.essarsoftware.par.engine.core.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Response class for {@code POST /players}.
 * @author @essar
 */
@ResponseStatus(code = HttpStatus.CREATED)
public class CreatePlayerResponse
{
    private final Player player;

    /**
     * Instantiate response.
     * @param player the player just created.
     */
    public CreatePlayerResponse(Player player) {

        this.player = player;

    }

    /**
     * Get player ID.
     * @return the player ID.
     */
    @JsonGetter("player_id")
    public String getPlayerID() {

        return player.getPlayerID();

    }

    /**
     * Get player name
     * @return the player name.
     */
    @JsonGetter("player_name")
    public String getPlayerName() {

        return player.getPlayerName();

    }

    /**
     * Get player state.
     * @return the player state.
     */
    @JsonGetter("player_state")
    public String getPlayerState() {

        return player.getPlayerState().name();

    }

    /**
     * Get the response as an ASCII table.
     * @return a String containing the response values.
     */
    public String asTable() {

        return String.format("%10S | %30S | %10S", "PLAYER ID", "PLAYER NAME", "STATE")
            + String.format("%n-----------+--------------------------------+------------")
            + String.format("%n%10s | %30s | %10s", getPlayerID(), getPlayerName(), getPlayerState());

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("Created Player %s", player.getPlayerID());

    }
}
