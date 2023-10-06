package uk.co.essarsoftware.par.engine.core.responses;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerList;

/**
 * Response class for {@code GET /game}.
 * @author @essar
 */
public class GetGameResponse
{

    private final Game game;
    private final PlayerList players;

    /**
     * Instantiate response.
     * @param game the current game.
     * @param players the list of players in the game.
     */
    public GetGameResponse(Game game, PlayerList players) {

        this.game = game;
        this.players = players;

    }

    /**
     * Get the current player.
     * @return a shortened form of the player object.
     */
    @JsonGetter("current_player")
    public PlayerStubResponse getCurrentPlayer() {

        Player currentPlayer = players.getCurrentPlayer();
        return new PlayerStubResponse(currentPlayer);

    }

    /**
     * Get the ID of the player who is currently the dealer.
     * @return the dealer ID.
     */
    @JsonGetter("dealer_id")
    public String getDealer() {

        return players.getDealer() == null ? null : players.getDealer().getPlayerID();
        
    }

    /**
     * Get the current round olf the game.
     * @return the current round identifier.
     */
    @JsonGetter("current_round")
    public Round getCurrentRound() {

        return game.getCurrentRound();

    }

    /**
     * Get the ID of the current game.
     * @return the game ID.
     */
    @JsonGetter("game_id")
    public String getGameID() {

        return game.getGameID();

    }

    /**
     * Get the number of players currently in the game.
     * @return the player count.
     */
    @JsonGetter("player_count")
    public Integer getPlayerCount() {

        return players.size();
        
    }

    /**
     * Get the current round turn.
     * @return the turn count.
     */
    @JsonGetter("turn_count")
    public Integer getTurnCount() {

        return game.getTurnCount();

    }

    /**
     * Get the response as an ASCII table.
     * @return a String containing the response values.
     */
    public String asTable() {

        return String.format("%30S | %10S | %4S | %30S", "GAME ID", "CURRENT ROUND", "TURN", "CURRENT PLAYER")
            + String.format("%n-----------+--------------------------------+------------+--------+-------------")
            + String.format("%n%30s | %10s | %4d | %30s", getGameID(), getCurrentRound(), getTurnCount(), getCurrentPlayer());

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("%s [%s]", getGameID(), getCurrentRound());

    }
}
