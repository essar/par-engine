package uk.co.essarsoftware.par.game;

import org.springframework.stereotype.Component;

@Component
public class Game
{
    private final String gameID;
    private int turnCount;
    private Round currentRound;

    public Game(String gameID) {

        this.gameID = gameID;
        this.currentRound = Round.START;
        this.turnCount = 0;

    }

    public Round getCurrentRound() {

        return currentRound;

    }

    public String getGameID() {

        return gameID;

    }

    public int getTurnCount() {

        return turnCount;

    }

    public void incrementTurnCount() {

        turnCount ++;

    }

    public void resetTurnCount() {

        turnCount = 1;

    }

    public void setCurrentRound(Round currentRound) {

        if (currentRound == null) {

            throw new IllegalArgumentException("Cannot set current round to null");

        }
        this.currentRound = currentRound;

    }

    @Override
    public String toString() {

        return String.format("Game [ID: %s; Round: %s; Turn: %d]", gameID, currentRound, turnCount);
    
    }
}
