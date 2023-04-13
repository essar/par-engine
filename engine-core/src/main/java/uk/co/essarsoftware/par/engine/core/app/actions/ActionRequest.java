package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ActionRequest<A extends Action<?>>
{

    private final Map<String, A> params = new HashMap<>();
    private ActionType actionType;
    private Integer actionSequence;
    private String gameID;

    @JsonSetter("action_params")
    void unpackParams(Map<String, A> params) {

        this.params.putAll(params);

    }

    public A getAction() {

        return actionType == null ? null : params.get(actionType.name().toLowerCase());

    }

    @JsonGetter("action_params")
    public Map<String, A> getActionParams() {

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

    public void setSequence(Integer actionSequence) {

        this.actionSequence = actionSequence;

    }

    public void setActionType(ActionType actionType) {

        this.actionType = actionType;

    }

    public void setGameID(String gameID) {

        this.gameID = gameID;

    }

    public String toString() {

        return String.format("%s (seq:%04x)", getAction(), actionSequence);

    }
}
