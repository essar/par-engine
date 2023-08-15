package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;

abstract class Action<R>
{
    
    private final Map<String, Object> params = new HashMap<>();
    private final Integer actionSequence;
    private final String playerID;

    protected Action(ActionRequest request) {

        if (Objects.isNull(request.getPlayerID())) {

            throw new InvalidActionRequestException("Missing player_id");

        }
        this.playerID = request.getPlayerID();

        if (Objects.isNull(request.getActionSequence())) {

            throw new InvalidActionRequestException("Missing action_sequence");

        }
        this.actionSequence = request.getActionSequence();

    }

    abstract void setResult(R card);

    protected void addActionParameter(String key, Object value) {

        if (value == null) {

            throw new InvalidActionRequestException(String.format("Missing parameter: %s", key));

        }
        params.put(key, value);

    }

    protected <E> void copyActionParameter(ActionRequest request, String key, Class<E> clz, boolean required) {

        copyActionParameter(request, key, clz, required, e -> e);

    }

    protected <E> void copyActionParameter(ActionRequest request, String key, Class<E> clz, boolean required, Function<E, ?> mappingFunction) {

        E value = request.getRequestParameter(key, clz);
        if (required && value == null) {

            throw new InvalidActionRequestException(String.format("Missing parameter: %s", key));

        }
        params.put(key, mappingFunction.apply(value));

    }

    Integer getActionSequence() {

        return actionSequence;
        
    }

    <E> E getActionParameter(String key, Class<E> clz) {

        return clz.cast(params.get(key));

    }

    boolean validateActionParameters() {

        return true;

    }

    public String getPlayerID() {

        return playerID;

    }


    public static class DiscardAction extends Action<Card>
    {

        public DiscardAction(ActionRequest request) {

            super(request);
            addActionParameter("card", CardEncoder.asCard(request.getRequestParameter("card", String.class)));

        }

        @Override
        void setResult(Card card) {

            addActionParameter("card", card);

        }

        @Override
        boolean validateActionParameters() {

            return Objects.nonNull(getCard());
            
        }

        public Card getCard() {

            return getActionParameter("card", Card.class);

        }
        
        @Override
        public String toString() {

            return String.format("%s discard %s", getPlayerID(), CardEncoder.asShortString(getCard()));

        }
    }

    public static class PickupDiscardAction extends Action<Card>
    {

        public PickupDiscardAction(ActionRequest request) {

            super(request);

        }

        @Override
        void setResult(Card card) {

            addActionParameter("card", card);

        }

        public Card getCard() {

            return getActionParameter("card", Card.class);

        }

        @Override
        public String toString() {

            return String.format("%s pickup from discard pile", getPlayerID());

        }
    }

    public static class PickupDrawAction extends Action<Card>
    {

        public PickupDrawAction(ActionRequest request) {

            super(request);

        }

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

    public static class PlayCardsAction extends Action<Play>
    {

        public PlayCardsAction(ActionRequest request) {

            super(request);
            copyActionParameter(request, "cards", ArrayList.class, true, CardEncoder::asCards);
            copyActionParameter(request, "joker_bindings", ArrayList.class, true, CardEncoder::asCards);
            
        }

        @Override
        void setResult(Play play) {

            addActionParameter("play", play);

        }

        public Card[] getCards() {

            return getActionParameter("cards", Card[].class);

        }

        public Card[] getJokerBindings() {

            return getActionParameter("joker_bindings", Card[].class);
            
        }

        @Override
        public String toString() {

            return String.format("%s playing cards %s", getPlayerID(), CardEncoder.asShortString(getCards()));

        }
    }
}