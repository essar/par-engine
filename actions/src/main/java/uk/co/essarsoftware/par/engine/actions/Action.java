package uk.co.essarsoftware.par.engine.actions;

import java.util.HashMap;
import java.util.Map;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Abstract class providing methods common to all game actions.
 * @param <E> type of object this action returns.
 * @author @essar
 */
public abstract class Action<E>
{

    private final Map<String, Object> params = new HashMap<>();
    private final Integer actionSequence;
    private final String playerID, requestID;

    /**
     * Instantiate a new Action object, with the specified parameters.
     * @param requestID a unique ID attached to the request used to trigger this action.
     * @param actionSequence the incremental sequence ID passed for this action.
     * @param playerID the ID of the player who invoked this action.
     */
    Action(final String requestID, final Integer actionSequence, final String playerID) {

        this.requestID = requestID;
        this.actionSequence = actionSequence;
        this.playerID = playerID;

    }

    /**
     * Get the result of this action.
     * @return the result of this action.
     */
    public abstract E getResult();

    /**
     * Run this action, using parameters that have been set and return a value (if any) accessible by {@link #getResult()}.
     * @param player the game player this action is being performed against.
     */
    public abstract void runAction(Player player);

    /**
     * Retrieve an action parameter set for this action.
     * @param <F> the type to return the parameter as.
     * @param key static name of the parameter.
     * @param clz the type to return the parameter as.
     * @return the parameter value.
     * @throws ClassCastException if the parameter cannot be converted into {@code clz}.
     */
    protected <F> F getParameter(String key, Class<F> clz) {

        return clz.cast(params.get(key));

    }

    /**
     * Set a parameter value.
     * @param key static name of the parameter.
     * @param value the parameter value.
     */
    protected void setParameter(String key, Object value) {

        params.put(key, value);

    }

    /**
     * Add a parameter to this action.
     * @param key static name of the parameter.
     * @param value the parameter value.
     */
    public void addActionParameter(String key, Object value) {

        setParameter(key, value);

    }

    /**
     * Get the sequence number attached to this action.
     * @return an incremental integer that should match the next expected action.
     */
    public Integer getActionSequence() {

        return actionSequence;

    }

    /**
     * Get the ID of the player who invoked this action.
     * @return ID of the player.
     */
    public String getPlayerID() {

        return playerID;

    }

    /**
     * Get the unique ID attached to the request used to trigger this action.
     * @return the ID as a String.
     */
    public String getRequestID() {

        return requestID;

    }
}
