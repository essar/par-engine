package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link KevinEvent} class.
 * @author @essar
 */
public class KevinEventTests
{

    @Test
    public void testGetCountReturnsExpectedCount() {

        KevinEvent event = new KevinEvent(2, 12345L);
        assertEquals(2, event.getCount(), "Expected event count to be 2");

    }

    @Test
    public void testGetLastEventTimestampReturnsExpectedTimestamp() {

        KevinEvent event = new KevinEvent(2, 12345L);
        assertEquals(12345L, event.getLastEventTimestamp(), "Expected event timestamp to be 12345");

    }

    @Test
    public void testGetMillisSinceLastEventReturnsZeroForMatchingCreationAndLastEvent() {

        final long ts = System.currentTimeMillis();
        KevinEvent event = new KevinEvent(ts, 2, ts);
        assertEquals(0, event.getMillisSinceLastEvent(), "Expected millis since last event to be greater than zero");
    
    }

    @Test
    public void testGetMillisSinceLastEventReturns1000ForCreationOneSecondAfterLastEvent() {

        final long ts = System.currentTimeMillis();
        KevinEvent event = new KevinEvent(ts, 2, ts - 1000);
        assertEquals(1000, event.getMillisSinceLastEvent(), "Expected millis since last event to be within a second");

    }

    @Test
    public void testToStringReturnsExpectedString() {

        final long ts = System.currentTimeMillis();
        KevinEvent event = new KevinEvent(2, ts);
        assertTrue(Pattern.matches("Kevin! [0-9]{1,3}ms since the last event, message 2", event.toString()), "Expected toString to return formatted string");

    }
}