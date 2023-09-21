package uk.co.essarsoftware.par.engine.events;

/**
 * Base class for events that take place inside the engine.
 * Events may be subscribed to by Event Processors, in order for them
 * to take action.
 * @author @essar
 */
public abstract class EngineEvent
{
    private final long eventCreatedTimestamp;
    private long eventDispatchedTimestamp;

    /**
     * Instantiate a new EngineEvent, specifying the creation time.
     * @param eventCreatedTimestamp created timestamp in milliseconds.
     */
    EngineEvent(final long eventCreatedTimestamp) {

        this.eventCreatedTimestamp = eventCreatedTimestamp;
        
    }

    /**
     * Instantiate a new EngineEvent.
     */
    protected EngineEvent() {

        this(System.currentTimeMillis());

    }

    /**
     * Get the time the event was created.
     * @return created timestamp in milliseconds.
     */
    public long getEventCreatedTimestamp() {

        return eventCreatedTimestamp;

    }

    /**
     * Get the time the event was dispatched.
     * @return dispatch timestamp in milliseconds.
     */
    public long getEventDispatchedTimestamp() {

        return eventDispatchedTimestamp;

    }

    /**
     * Set the time the event was dispatched.
     * @param eventDispatchedTimestamp disatch timestamp in milliseconds.
     */
    public void setEventDispatchedTimestamp(long eventDispatchedTimestamp) {

        this.eventDispatchedTimestamp = eventDispatchedTimestamp;

    }
}
