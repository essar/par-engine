package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.engine.actions.Action;

public class ActionResponse
{

    private final Action<?> action;
    private ActionType actionType;
    private int actionSequence;
    private int nextActionSequence;

    public <A extends Action<?>> ActionResponse(final A action) {

        this.action = action;

        // Initialise from action object
        actionSequence = action.getActionSequence();
        //actionType = action.getActionType();
     
    }

    void setNextActionSequence(int nextActionSequence) {

        this.nextActionSequence = nextActionSequence;
    
    }

    @JsonGetter("action_params")
    public Map<String, Action<?>> getActionParams() {
        
        return actionType == null ? null : Collections.singletonMap(actionType.name().toLowerCase(), action);

    }

    @JsonGetter("action_sequence")
    public int getActionSequence() {

        return actionSequence;

    }

    @JsonGetter("action_type")
    public ActionType getActionType() {

        return actionType;

    }

    @JsonGetter("next_sequence")
    public int getNextActionSequence() {

        return nextActionSequence;

    }
}
