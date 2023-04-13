package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;

abstract class Action<R>
{
    
    private final Map<String, Object> params = new HashMap<>();
    private String playerID;

    abstract void setResult(R card);

    boolean validateAction() {

        return true;

    }

    @JsonAnySetter
    public void addActionParameter(String key, Object value) {

        params.put(key, value);

    }

    public <E> E getActionParameter(String key, Class<E> clz) {

        return clz.cast(params.get(key));

    }

    @JsonGetter("player_id")
    public String getPlayerID() {

        return playerID;

    }

    public void setPlayerID(String playerID) {

        this.playerID = playerID;

    }


    public static class DiscardAction extends Action<Card>
    {
        @Override
        void setResult(Card card) {

            setCard(card);

        }

        @Override
        boolean validateAction() {

            return Objects.nonNull(getCard());
            
        }

        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        @JsonSetter("card")
        public void setCard(Card card) {

            addActionParameter("card", card);

        }
        
        @Override
        public String toString() {

            return String.format("%s discard %s", getPlayerID(), CardEncoder.asShortString(getCard()));

        }
    }

    public static class PickupDiscardAction extends Action<Card>
    {
        @Override
        void setResult(Card card) {

            addActionParameter("card", card);

        }

        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        public String toString() {

            return String.format("%s pickup from discard pile", getPlayerID());

        }
    }

    public static class PickupDrawAction extends Action<Card>
    {
        @Override
        void setResult(Card card) {

            addActionParameter("card", card);

        }

        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        @Override
        public String toString() {

            return String.format("%s pickup from draw pile", getPlayerID());

        }
    }
}