package uk.co.essarsoftware.par.engine.core.app;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.cards.Card;

public class ActionRequest
{

    private ActionType actionType;
    private Integer actionSequence;
    private String playerID;

    private final Map<String, Object> actionParameters = new HashMap<>();


    @JsonAnySetter
    public void addActionParameter(String key, Object value) {

        actionParameters.put(key, value);

    }

    public <E> E getActionParameter(String key, Class<E> clz) {

        return clz.cast(actionParameters.get(key));

    }

    @JsonAnyGetter
    public Map<String, Object> getActionParameters() {

        return actionParameters;

    }

    @JsonGetter("action_sequence")
    public Integer getActionSequence() {

        return actionSequence;

    }
    
    @JsonGetter("action_type")
    public ActionType getActionType() {

        return actionType;
    
    }

    @JsonGetter("player_id")
    public String getPlayerID() {

        return playerID;

    }

    public void setSequence(Integer actionSequence) {

        this.actionSequence = actionSequence;

    }

    public void setActionType(ActionType actionType) {

        this.actionType = actionType;

    }

    public void setPlayerID(String playerID) {

        this.playerID = playerID;

    }

    public String toString() {

        return String.format("%s wishes to %s (seq:%d)", playerID, actionType, actionSequence);

    }

    public static class DiscardActionRequest extends ActionRequest
    {
        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        public void setCard(Card card) {

            addActionParameter("card", card);

        }
    }

    public static class PickupDiscardActionRequest extends ActionRequest
    {
        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        public void setCard(Card card) {

            addActionParameter("card", card);

        }
    }

    public static class PickupDrawActionRequest extends ActionRequest
    {
    }
}
