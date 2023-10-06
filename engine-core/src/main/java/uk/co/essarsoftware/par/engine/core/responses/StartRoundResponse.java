package uk.co.essarsoftware.par.engine.core.responses;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.game.Round;

public class StartRoundResponse
{

    private final Round round;
    private final String currentPlayerID;

    public StartRoundResponse(Round round, String currentPlayerID) {

        this.round = round;
        this.currentPlayerID = currentPlayerID;

    }

    @JsonGetter("current_player")
    public String getCurrentPlayerID() {

        return currentPlayerID;
    }

    public Round getRound() {

        return round;

    } 
}
