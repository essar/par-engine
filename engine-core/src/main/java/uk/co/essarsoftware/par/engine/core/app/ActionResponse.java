package uk.co.essarsoftware.par.engine.core.app;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.DiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDrawActionRequest;

public abstract class ActionResponse
{

    private final HashMap<String, Object> responseParams = new HashMap<>();
    private int actionSequence, nextActionSequence;
    private ActionType actionType;

    protected ActionResponse(ActionRequest request) {

        actionSequence = request.getActionSequence();
        actionType = request.getActionType();
     
    }

    protected void addParameter(String paramName, Object paramValue) {

        responseParams.put(paramName, paramValue);

    }

    protected <E> E getParameter(String paramName, Class<E> clz) {

        return clz.cast(responseParams.get(paramName));

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

    public void setNextActionSequence(int nextActionSequence) {

        this.nextActionSequence = nextActionSequence;
    
    }    

    public static class DiscardActionResponse extends ActionResponse
    {

        public DiscardActionResponse(DiscardActionRequest request) {

            super(request);
            
        }

        @JsonGetter("card")
        public Card getCard() {

            return getParameter("card", Card.class);

        }

        public void setCard(Card card) {

            addParameter("card", card);

        }
    }  

    public static class PickupDiscardActionResponse extends ActionResponse
    {

        public PickupDiscardActionResponse(PickupDiscardActionRequest request) {

            super(request);

        }

        @JsonGetter("card")
        public Card getCard() {

            return getParameter("card", Card.class);

        }

        public void setCard(Card card) {

            addParameter("card", card);

        }
    }   

    public static class PickupDrawActionResponse extends ActionResponse
    {

        public PickupDrawActionResponse(PickupDrawActionRequest request) {

            super(request);

        }
        
        @JsonGetter("card")
        public Card getCard() {

            return getParameter("card", Card.class);

        }

        public void setCard(Card card) {

            addParameter("card", card);

        }
    }
}
