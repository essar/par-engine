package uk.co.essarsoftware.par.engine.core.app.players;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.game.Player;

public class GetPlayerResponse
{
    private Player player;

    GetPlayerResponse(Player player) {

        this.player = player;

    }

    @JsonGetter("hand_size")
    public Integer getHandSize() {

        return player.getHandSize();

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

    @JsonGetter("has_penalty_card")
    public Boolean hasPenaltyCard() {

        return player.hasPenaltyCard();

    }

    @JsonGetter("is_down")
    public Boolean isDown() {

        return player.isDown();

    }

    public String asTable() {

        return String.format("%10S | %30S | %10S | %6S | %12S", "PLAYER ID", "PLAYER NAME", "STATE", "HAND", "FLAGS")
            + String.format("%n-----------+--------------------------------+------------+--------+-------------")
            + String.format("%n%10s | %30s | %10s | %6d | %12s", getPlayerID(), getPlayerName(), getPlayerState(), getHandSize(), String.join(",", player.getFlags()));

    }

    @Override
    public String toString() {

        return player.toString();

    }
}
