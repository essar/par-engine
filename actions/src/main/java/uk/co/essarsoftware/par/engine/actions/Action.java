package uk.co.essarsoftware.par.engine.actions;

import java.util.HashMap;
import java.util.Map;

import uk.co.essarsoftware.par.engine.players.Player;

public abstract class Action<E>
{

    private final Map<String, Object> params = new HashMap<>();
    private final Integer actionSequence;
    private final String playerID, requestID;

    Action(String requestID, Integer actionSequence, String playerID) {

        this.requestID = requestID;
        this.actionSequence = actionSequence;
        this.playerID = playerID;

    }

    public abstract E getResult();

    public abstract void runAction(Player player);

    <F> F getParameter(String key, Class<F> clz) {

        return clz.cast(params.get(key));

    }

    void setParameter(String key, Object value) {

        params.put(key, value);

    }

    public void addActionParameter(String key, Object value) {

        setParameter(key, value);

    }

    public Integer getActionSequence() {

        return actionSequence;

    }

    public String getPlayerID() {

        return playerID;

    }

    public String getRequestID() {

        return requestID;

    }
}
