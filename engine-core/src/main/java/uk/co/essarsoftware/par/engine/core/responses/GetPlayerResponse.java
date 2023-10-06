package uk.co.essarsoftware.par.engine.core.responses;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.players.Player;

public class GetPlayerResponse
{
    private Player player;

    public GetPlayerResponse(Player player) {

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

    @Override
    public String toString() {

        return String.format("%s:  %s [%s] %d card(s)  %s", getPlayerID(), getPlayerName(), getPlayerState(), getHandSize(), String.join(",", player.getFlags()));

    }
}
