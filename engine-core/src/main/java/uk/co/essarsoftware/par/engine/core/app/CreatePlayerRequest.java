package uk.co.essarsoftware.par.engine.core.app;

import com.fasterxml.jackson.annotation.JsonGetter;

public class CreatePlayerRequest
{

    private String playerName;

    @JsonGetter("player_name")
    public String getPlayerName() {

        return playerName;
    
    }
    
    public void setPlayerName(String playerName) {

        this.playerName = playerName;

    }
    
}
