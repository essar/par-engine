package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link EngineEvent} class.
 * @author @essar
 */
public class EngineEventTests
{

    @Test
    public void testGetEventCreatedTimestampReturnsExpectedMillis() {

        final long ts = System.currentTimeMillis();
        EngineEvent event = new TestEngineEvent();
        assertTrue(event.getEventCreatedTimestamp() >= ts, "Expected event created timestamp to be close to current time");

    }

    @Test
    public void testGetEventDispatchedTimestampReturnsExpectedMillis() {

        EngineEvent event = new TestEngineEvent();
        event.setEventDispatchedTimestamp(12345);
        assertEquals(event.getEventDispatchedTimestamp(), 12345L, "Expected event created timestamp to be 12345");

    }

    private class TestEngineEvent extends EngineEvent
    {
        TestEngineEvent() {

            super();

        }
    }
}