package uk.co.essarsoftware.par.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Game
{

    @Autowired
    private PlayerList players;

    private final String gameID;
    private int turnCount;
    private Round currentRound;

    public Game(String gameID) {

        this.gameID = gameID;
        this.currentRound = Round.START;
        this.turnCount = 0;

    }

    public Player getCurrentPlayer() {

        if (getCurrentRound() == Round.START || getCurrentRound() == Round.END) {

            return null;

        } else {

            return players == null ? null : players.getCurrentPlayer();

        }        
    }

    public Round getCurrentRound() {

        return currentRound;

    }

    public Player getDealer() {

        if (getCurrentRound() == Round.START || getCurrentRound() == Round.END) {

            return null;

        } else {

            return players == null ? null : players.getDealer();

        }
    }

    public String getGameID() {

        return gameID;

    }

    public int getPlayerCount() {

        return players == null ? 0 : players.size();

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
