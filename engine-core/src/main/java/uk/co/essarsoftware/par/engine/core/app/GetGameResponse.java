package uk.co.essarsoftware.par.engine.core.app;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.game.Game;
import uk.co.essarsoftware.par.game.Player;
import uk.co.essarsoftware.par.game.PlayerList;
import uk.co.essarsoftware.par.game.Round;

public class GetGameResponse
{

    private Game game;
    private PlayerList players;

    GetGameResponse setGame(Game game) {

        this.game = game;
        return this;

    }

    GetGameResponse setPlayers(PlayerList players) {

        this.players = players;
        return this;

    }

    @JsonGetter("current_player")
    public Player getCurrentPlayer() {

        return players == null ? null : players.getCurrentPlayer();

    }

    @JsonGetter("dealer")
    public Player getDealer() {

        return players == null ? null : players.getDealer();
        
    }

    @JsonGetter("current_round")
    public Round getCurrentRound() {

        return game == null ? null : game.getCurrentRound();

    }

    @JsonGetter("game_id")
    public String getGameID() {

        return game == null ? null : game.getGameID();

    }

    @JsonGetter("player_count")
    public Integer getPlayerCount() {

        return players == null ? null : players.size();
        
    }

    

    @JsonGetter("turn_count")
    public Integer getTurnCount() {

        return game == null ? null : game.getTurnCount();

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
