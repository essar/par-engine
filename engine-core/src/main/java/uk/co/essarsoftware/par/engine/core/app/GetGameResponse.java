package uk.co.essarsoftware.par.engine.core.app;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.core.app.players.PlayerStubResponse;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerList;

public class GetGameResponse
{

    private final Game game;
    private final PlayerList players;

    public GetGameResponse(Game game, PlayerList players) {

        this.game = game;
        this.players = players;

    }

    @JsonGetter("current_player")
    public PlayerStubResponse getCurrentPlayer() {

        Player currentPlayer = players.getCurrentPlayer();
        return new PlayerStubResponse(currentPlayer);

    }

    @JsonGetter("dealer_id")
    public String getDealer() {

        return players.getDealer() == null ? null : players.getDealer().getPlayerID();
        
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

        return players.size();
        
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
