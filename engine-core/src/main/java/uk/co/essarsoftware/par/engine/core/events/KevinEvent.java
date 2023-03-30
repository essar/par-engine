package uk.co.essarsoftware.par.engine.core.events;

public class KevinEvent extends EngineEvent
{

    private final int kevinCount;
    private final long lastEventTimestamp, millisSinceLastEvent;

    public KevinEvent(int kevinCount, long lastEventTimestamp) {

        this.kevinCount = kevinCount;
        this.lastEventTimestamp = lastEventTimestamp;
        this.millisSinceLastEvent = System.currentTimeMillis() - lastEventTimestamp;

    }

    public int getCount() {

        return kevinCount;

    }

    public long getLastEventTimestamp() {

        return lastEventTimestamp;

    }

    public long getMillisSinceLastEvent() {

        return millisSinceLastEvent;

    }

    @Override
    public String toString() {

        return String.format("Kevin! %,dms since the last event, message %d", millisSinceLastEvent, kevinCount);

    }
}
