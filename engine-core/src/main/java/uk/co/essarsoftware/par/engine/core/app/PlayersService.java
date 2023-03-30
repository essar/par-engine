package uk.co.essarsoftware.par.engine.core.app;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.engine.core.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.core.events.PlayerStateChangeEvent;
import uk.co.essarsoftware.par.game.Game;
import uk.co.essarsoftware.par.game.Player;
import uk.co.essarsoftware.par.game.PlayerList;
import uk.co.essarsoftware.par.game.PlayerState;
import uk.co.essarsoftware.par.game.Round;

@Service
public class PlayersService
{

    @Autowired
    private EngineEventQueue eventQueue;

    @Autowired
    private Game game;
    
    @Autowired
    private PlayerList players;


    private String generatePlayerID() {

        String playerID;
        do {
            playerID = UUID.randomUUID().toString();
            playerID = playerID.substring(playerID.length() - 10);
        } while (players.containsPlayer(playerID));
        
        return playerID;

    }
    

    public void addPlayer(Player player) {

        if (game.getCurrentRound() != Round.START) {

            throw new GameStateException("Cannot add a player to a game in progress");

        }
        synchronized (players) {     

            players.add(player);

        }
    }

    public Player createPlayer(String playerName) {

        Player player = new Player(generatePlayerID(), playerName);
        addPlayer(player);

        return player;
    
    }

    public Player getPlayer(final String playerID) {

        return getPlayersStream()
            .filter(p -> Objects.equals(p.getPlayerID(), playerID))
            .findFirst().orElseThrow(UnknownPlayerException::new);

    }

    public Stream<Player> getPlayersStream() {

        return players.stream();

    }

    public Player getCurrentPlayer() {

        //synchronized (players) {

            return players.isEmpty() ? null : players.getCurrentPlayer();

        //}
    }

    public Player getDealer() {

        //synchronized (players) {

            return players.isEmpty() ? null : players.getDealer();

        //}
    }

    public Player getNextPlayer() {

        //synchronized (players) {

            return players.isEmpty() ? null : players.getNextPlayer();

        //}
    }

    public boolean isCurrentPlayer(String playerID) {

        return !players.isEmpty() && Objects.equals(getCurrentPlayer().getPlayerID(), playerID);

    }

    public boolean isCurrentPlayerInState(PlayerState playerState) {

        return !players.isEmpty() && getCurrentPlayer().getPlayerState() == playerState;
            
    }

    public Player nextPlayer() {

        // Check current player state
        if (!isCurrentPlayerInState(PlayerState.WATCHING)) {

            throw new InvalidPlayerStateException("Cannot move to next player; not in WATCHING state");

        }

        // Move to next player
        Player nextPlayer = players.toNextPlayer();

        return nextPlayer;

    }

    public void removePlayer(Player player) {

        if (game.getCurrentRound() != Round.START) {

            throw new GameStateException("Cannot remove a player from a game in progress");
            
        }
        synchronized (players) {

            players.remove(player);

        }
    }

    public void setPlayerState(Player player, PlayerState playerState) {

        PlayerState oldState = player.getPlayerState();
        player.setPlayerState(playerState);

        // Queue event
        eventQueue.queueEvent(new PlayerStateChangeEvent(player, oldState, playerState));

    }

    public void startNewRound() {

        // Check all players have appropriate states
        if (!(game.getCurrentRound() == Round.START && getPlayersStream().allMatch(p -> p.getPlayerState() == PlayerState.INIT))) {

            // Not all players are in INIT state
            throw new InvalidPlayerStateException(String.format("Cannot start new round; not all players are in INIT state"));

        }
        if (getPlayersStream().anyMatch(p -> p.getPlayerState() == PlayerState.FINISHED)) {

            // No player is in FINISH state
            throw new InvalidPlayerStateException("Cannot start new round; no player is in FINISH state");

        }

        if (players.size() < 2) {

            throw new GameStateException("Cannot begin a new round with fewer than two players");

        }

        if (game.getCurrentRound() == Round.START) {

            // Randomise the start player
            players.shufflePlayers();

        } else {

            // Move current player and dealer pointers
            players.newRound();

        }
    }

    public Player validateIsCurrentPlayerAndInState(String playerID, PlayerState playerState) {

        // Check specified player is current player
        if (!isCurrentPlayer(playerID)) {

            throw new NotCurrentPlayerException(String.format("Player %s is not the current player", playerID));

        }
        // Check current player is in correct status
        if (!isCurrentPlayerInState(playerState)) {

            throw new InvalidPlayerStateException(String.format("Player %s expected to be in %s state", playerID, playerState));
            
        }

        // Return the current player
        return getCurrentPlayer();

    }
}
