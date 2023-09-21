package uk.co.essarsoftware.par.engine.events;

/**
 * {@link EngineEvent} instance generated when a Player is taking too long over their turn and no action has been observed in the engine for the play timeout period.
 * @author @essar
 */
public class KevinEvent extends EngineEvent
{

    private final int kevinCount;
    private final long lastEventTimestamp, millisSinceLastEvent;

    /**
     * Instantiate a new KevinEvent.
     * @param eventCreatedTimestamp created timestamp in milliseconds.
     * @param kevinCount the number of KevinEvents that have been raised since the last action event.
     * @param lastEventTimestamp Epoch timestamp of the last observed event.
     */
    KevinEvent(long eventCreatedTimestamp, int kevinCount, long lastEventTimestamp) {

        super(eventCreatedTimestamp);
        this.kevinCount = kevinCount;
        this.lastEventTimestamp = lastEventTimestamp;
        this.millisSinceLastEvent = getEventCreatedTimestamp() - lastEventTimestamp;

    }

    /**
     * Instantiate a new KevinEvent.
     * @param kevinCount the number of KevinEvents that have been raised since the last action event.
     * @param lastEventTimestamp Epoch timestamp of the last observed event.
     */
    public KevinEvent(int kevinCount, long lastEventTimestamp) {

        this.kevinCount = kevinCount;
        this.lastEventTimestamp = lastEventTimestamp;
        this.millisSinceLastEvent = getEventCreatedTimestamp() - lastEventTimestamp;

    }

    /**
     * Get the number of KevinEvents observed.
     * @return the cunt of KevinEvents.
     */
    public int getCount() {

        return kevinCount;

    }

    /**
     * Get the epoch timestamp of the last observed event.
     * @return the time in milliseconds.
     */
    public long getLastEventTimestamp() {

        return lastEventTimestamp;

    }

    /**
     * Get the number of milliseconds between the last observed event and the time this event was created.
     * @return the time in milliseconds.
     */
    public long getMillisSinceLastEvent() {

        return millisSinceLastEvent;

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("Kevin! %,dms since the last event, message %d", millisSinceLastEvent, kevinCount);

    }
}
