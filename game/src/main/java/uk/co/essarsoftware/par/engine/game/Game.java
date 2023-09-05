package uk.co.essarsoftware.par.engine.game;

import org.springframework.stereotype.Component;

/**
 * Class representing a Game.
 */
@Component
public class Game
{

    private final String gameID;
    private int turnCount;
    private Round currentRound;

    /**
     * Instantiate a new Game with a specified game ID.
     * @param gameID a String that uniquely identifies this game.
     */
    public Game(String gameID) {

        this.gameID = gameID;
        this.currentRound = Round.START;
        this.turnCount = 0;

    }

    /**
     * Get the current round being played in this game.
     * @return the current round.
     */
    public Round getCurrentRound() {

        return currentRound;

    }

    /**
     * Get the game identifier.
     * @return a String that uniquely identifies this game.
     */
    public String getGameID() {

        return gameID;

    }

    /**
     * Get the current turn count.
     * @return the current turn count.
     */
    public int getTurnCount() {

        return turnCount;

    }

    /**
     * Increment the turn counter; normally called after the dealer has had their turn.
     */
    public void incrementTurnCount() {

        turnCount ++;

    }

    /**
     * Reset the turn counter to 1; normally called at the start of each round.
     */
    public void resetTurnCount() {

        turnCount = 1;

    }

    /**
     * Set the current game round.
     * @param currentRound the new round being played in this game.
     */
    public void setCurrentRound(Round currentRound) {

        if (currentRound == null) {

            throw new IllegalArgumentException("Cannot set current round to null");

        }
        this.currentRound = currentRound;

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("Game@%s[round=%s,turn=%d]", gameID, currentRound, turnCount);
    
    }
}
