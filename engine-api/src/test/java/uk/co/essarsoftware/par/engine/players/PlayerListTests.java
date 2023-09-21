package uk.co.essarsoftware.par.engine.players;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayerListTests
{

    private static Player[] players;

    @BeforeAll
    public static void initPlayers() {

        players = new Player[] {
            new Player("test1", "Test Player 1"),
            new Player("test2", "Test Player 2"),
            new Player("test3", "Test Player 3")
        };
        
    }

    private static Player firstPlayer() {

        return players[0];

    }

    private static Player lastPlayer() {

        return players[players.length - 1];

    }

    private static Player nextPlayer() {

        return players[1];

    }

    @Test
    public void testContainsPlayerReturnsFalseForEmptyList() {

        PlayerList playerList = new PlayerList();
        assertFalse(playerList.containsPlayer(firstPlayer().getPlayerID()), "Expected contains to be false for an empty PlayerList");

    }

    @Test
    public void testContainsPlayerReturnsTrueIfContainsPlayer() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertTrue(playerList.containsPlayer(firstPlayer().getPlayerID()), "Expected contains to be true if Player is in PlayerList");
        
    }

    @Test
    public void testContainsPlayerReturnsFalseIfDoesNotContainPlayer() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).dropWhile(firstPlayer()::equals).forEach(playerList::add);
        assertFalse(playerList.containsPlayer(firstPlayer().getPlayerID()), "Expected contains to be false if Player not in PlayerList");

    }

    @Test
    public void testGetCurrentPlayerReturnsNullForEmptyList() {

        PlayerList playerList = new PlayerList();
        assertNull(playerList.getCurrentPlayer(), "Expected current player to be the null from an empty list");
        
    }

    @Test
    public void testGetCurrentPlayerReturnsFirstInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertEquals(firstPlayer(), playerList.getCurrentPlayer(), "Expected current player to be the first player in the list");
        
    }

    @Test
    public void testGetDealerReturnsNullForEmptyList() {

        PlayerList playerList = new PlayerList();
        assertNull(playerList.getDealer(), "Expected dealer to be the null from an empty list");
        
    }

    @Test
    public void testGetDealerReturnsLastInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertEquals(lastPlayer(), playerList.getDealer(), "Expected dealer to be the last player in the list");
        
    }

    @Test
    public void testGetNextPlayerReturnsNullForEmptyList() {

        PlayerList playerList = new PlayerList();
        assertNull(playerList.getNextPlayer(), "Expected next player to be the null from an empty list");
        
    }

    @Test
    public void testGetNextPlayerReturnsFirstInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertEquals(nextPlayer(), playerList.getNextPlayer(), "Expected next player to be the second player in the list");
        
    }

    @Test
    public void testShufflePlayersRetainsAllCards() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        playerList.shufflePlayers();
        assertTrue(playerList.containsAll(Arrays.asList(players)), "Expected all players to be in list after shuffle");

    }

    @Test
    public void testShuffleResetsCurrentPlayerToFirstInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        playerList.toNextPlayer();
        playerList.shufflePlayers();
        assertEquals(playerList.getFirst(), playerList.getCurrentPlayer(), "Expected current player to be first player in the list");

    }

    @Test
    public void testShufflePlayersWithSameSeedHasSameResult() {

        PlayerList playerList1 = new PlayerList();
        Arrays.stream(players).forEach(playerList1::add);
        playerList1.shufflePlayers(new Random(0L));
        Player[] firstShuffle = playerList1.stream().toArray(Player[]::new);

        PlayerList playerList2 = new PlayerList();
        Arrays.stream(players).forEach(playerList2::add);
        playerList2.shufflePlayers(new Random(0L));
        Player[] secondShuffle = playerList2.stream().toArray(Player[]::new);
        assertArrayEquals(firstShuffle, secondShuffle, "Expected both shuffles to provide same result");

    }

    @Test
    public void testShufflePlayersWithDifferentSeedHasDifferentResult() {

        PlayerList playerList1 = new PlayerList();
        Arrays.stream(players).forEach(playerList1::add);
        playerList1.shufflePlayers(new Random(0L));
        Player[] firstShuffle = playerList1.stream().toArray(Player[]::new);

        PlayerList playerList2 = new PlayerList();
        Arrays.stream(players).forEach(playerList2::add);
        playerList2.shufflePlayers(new Random(1L));
        Player[] secondShuffle = playerList2.stream().toArray(Player[]::new);
        assertFalse(Arrays.equals(firstShuffle, secondShuffle), "Expected shuffles to provide different result");

    }

    @Test
    public void testToNextPlayerReturnsNextInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertEquals(nextPlayer(), playerList.toNextPlayer(), "Expected next player to be the next player in the list");

    }

    @Test
    public void testToNextPlayerSetsCurrentPlayerAsNextInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        playerList.toNextPlayer();
        assertEquals(nextPlayer(), playerList.getCurrentPlayer(), "Expected current player to be set to next player in the list");
        
    }

    @Test
    public void testToNextPlayerWrapsAtEndOfListAndReturnsFirstPlayer() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        Arrays.stream(players).forEach(p -> playerList.toNextPlayer());
        assertEquals(firstPlayer(), playerList.getCurrentPlayer(), "Expected player index to wrap back to first player in the list");
        
    }

    @Test
    public void testNewRoundReturnsNextPlayer() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        assertEquals(nextPlayer(), playerList.newRound(), "Expected next player to be next in list");
        
    }
    
    @Test
    public void testNewRoundSetsCurrentPlayerAsNextInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        playerList.toNextPlayer();
        playerList.newRound();
        assertEquals(nextPlayer(), playerList.getCurrentPlayer(), "Expected current player to be next in list");
        
    }

    @Test
    public void testNewRoundMovesDealerToNextInList() {

        PlayerList playerList = new PlayerList();
        Arrays.stream(players).forEach(playerList::add);
        playerList.newRound();
        assertEquals(firstPlayer(), playerList.getDealer(), "Expected dealer to be first in list");

    }

}
