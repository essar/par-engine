package uk.co.essarsoftware.par.engine.core.requests;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.core.app.actions.ActionType;

public class ActionRequest
{

    private final Map<String, Object> params = new HashMap<>();
    private final String requestID = UUID.randomUUID().toString();
    private ActionType actionType;
    private Integer actionSequence;
    private String gameID, playerID;

    @JsonAnySetter
    public void addRequestParameter(String key, Object value) {

        params.put(key, value);

    }

    @JsonGetter("action_params")
    public Map<String, Object> getActionParams() {

        return params;

    }

    @JsonGetter("action_sequence")
    public Integer getActionSequence() {

        return actionSequence;

    }
    
    @JsonGetter("action_type")
    public ActionType getActionType() {

        return actionType;
    
    }

    @JsonGetter("game_id")
    public String getGameID() {

        return gameID;

    }

    @JsonGetter("player_id")
    public String getPlayerID() {

        return playerID;

    }

    @JsonGetter("request_id")
    public String getRequestID() {

        return requestID;

    }

    public <E> E getRequestParameter(String key, Class<E> clz) {

        return clz.cast(params.get(key));

    }

    public void setSequence(Integer actionSequence) {

        this.actionSequence = actionSequence;

    }

    public void setActionType(ActionType actionType) {

        this.actionType = actionType;

    }

    public void setGameID(String gameID) {

        this.gameID = gameID;

    }

    public void setPlayerID(String playerID) {

        this.playerID = playerID;

    }

    public String toString() {

        return String.format("%s (seq:%04x)", getActionType(), getActionSequence());

    }
}
