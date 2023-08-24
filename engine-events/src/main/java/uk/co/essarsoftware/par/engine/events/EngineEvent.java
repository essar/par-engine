package uk.co.essarsoftware.par.engine.events;

public abstract class EngineEvent
{
    private final long eventCreatedTimestamp = System.currentTimeMillis();
    private long eventDispatchedTimestamp;

    public long getEventCreatedTimestamp() {

        return eventCreatedTimestamp;

    }

    public long getEventDispatchedTimestamp() {

        return eventDispatchedTimestamp;

    }

    public void setEventDispatchedTimestamp(long eventDispatchedTimestamp) {

        this.eventDispatchedTimestamp = eventDispatchedTimestamp;

    }
}
