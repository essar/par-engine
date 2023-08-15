package uk.co.essarsoftware.par.engine.core.app;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.game.Game;
import uk.co.essarsoftware.par.game.Player;
import uk.co.essarsoftware.par.game.Round;

public class GetGameResponse
{

    private final Game game;

    public GetGameResponse(Game game) {

        this.game = game;

    }

    @JsonGetter("current_player")
    public Map<String, Object> getCurrentPlayer() {

        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer == null ? null : Map.of(
            "player_id", currentPlayer.getPlayerID(),
            "player_name", currentPlayer.getPlayerName(),
            "player_state", currentPlayer.getPlayerState()
        );

    }

    @JsonGetter("dealer_id")
    public String getDealer() {

        return game.getDealer() == null ? null : game.getDealer().getPlayerID();
        
    }

    @JsonGetter("current_round")
    public Round getCurrentRound() {

        return game.getCurrentRound();

    }

    @JsonGetter("game_id")
    public String getGameID() {

        return game.getGameID();

    }

    @JsonGetter("player_count")
    public Integer getPlayerCount() {

        return game.getPlayerCount();
        
    }

    @JsonGetter("turn_count")
    public Integer getTurnCount() {

        return game.getTurnCount();

    }

    public String asTable() {

        return String.format("%30S | %10S | %4S | %30S", "GAME ID", "CURRENT ROUND", "TURN", "CURRENT PLAYER")
            + String.format("%n-----------+--------------------------------+------------+--------+-------------")
            + String.format("%n%30s | %10s | %4d | %30s", getGameID(), getCurrentRound(), getTurnCount(), getCurrentPlayer());

    }

    @Override
    public String toString() {

        return String.format("%s [%s]", getGameID(), getCurrentRound());

    }
}
