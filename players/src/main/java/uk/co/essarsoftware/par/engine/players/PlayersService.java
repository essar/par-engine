package uk.co.essarsoftware.par.engine.players;

import java.util.stream.Stream;

public interface PlayersService
{
    public void addPlayer(Player player);

    public Player createPlayer(String playerName);

    public Player getPlayer(final String playerID);

    public Stream<Player> getPlayersStream();

    public Player getCurrentPlayer();

    public Player getDealer();

    public Player getNextPlayer();

    public int getPlayerCount();

    public boolean isCurrentPlayer(String playerID);

    public boolean isCurrentPlayerInState(PlayerState playerState);

    public Player nextPlayer();

    public void removePlayer(Player player);

    public void setPlayerState(Player player, PlayerState playerState);

    public void startNewRound();

    public Player validateIsCurrentPlayerAndInState(String playerID, PlayerState playerState);
    
}
