package uk.co.essarsoftware.par.engine.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link Game} class.
 * @author @essar
 */
public class GameTests
{
    
    @Test
    public void testGetCurrentRoundReturnsStartForNewGame() {

        Game game = new Game("test-game");
        assertEquals(Round.START, game.getCurrentRound(), "currentRound is not expected value");

    }

    @Test
    public void testGetCurrentRoundReturnsExpectedRound() {

        Round round = Round.PP;
        Game game = new Game("test-game");
        game.setCurrentRound(round);
        assertEquals(round, game.getCurrentRound(), "currentRound is not expected value");

    }

    @Test
    public void testGetGameIDReturnsExpectedValue() {

        String gameID = "test-game";
        Game game = new Game(gameID);
        assertEquals(gameID, game.getGameID(), "gameID is not expected value");

    }

    @Test
    public void testGetTurnCountReturnsZeroForNewGame() {

        Game game = new Game("test-game");
        assertEquals(0, game.getTurnCount(), "turnCount is not expected value");

    }

    @Test
    public void testGetTurnCountForNewGameReturnsOneAfterIncrementTurnCount() {

        Game game = new Game("test-game");
        game.incrementTurnCount();
        assertEquals(1, game.getTurnCount(), "turnCount is not expected value");

    }

    @Test
    public void testGetTurnCountForNewGameReturnsOneAfterResetTurnCount() {

        Game game = new Game("test-game");
        game.resetTurnCount();
        assertEquals(1, game.getTurnCount(), "turnCount is not expected value");

    }

    @Test
    public void testSetCurrentRoundThrowsExceptionWithNullRound() {

        Game game = new Game("test-game");
        assertThrows(IllegalArgumentException.class, () -> game.setCurrentRound(null), "Expected IllegalArgumentException");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Game game = new Game("test-game");
        game.setCurrentRound(Round.PP);
        game.resetTurnCount();
        assertEquals("Game@test-game[round=PP,turn=1]", game.toString(), "toString is not expected value");
        
    }
}
