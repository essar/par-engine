package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test cases for {@link EngineEventQueue}.
 * @author @essar
 */
public class EngineEventQueueTests
{

    private EventDispatcherExecutorService mockDispatchService;
    private EventProcessor mockEventProcessor;

    @BeforeEach
    public void mockEventDispatcher() {

        mockDispatchService = Mockito.mock(EventDispatcherExecutorService.class);

    }

    @BeforeEach
    public void mockEventProcessor() {

        mockEventProcessor = Mockito.mock(EventProcessor.class);

    }

    @Test
    public void testStartDispatcherRunsDispatcher() {

        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, mockEventProcessor);
        queue.startDispatcher();
        // Brief pause to allow dispatcher to run
        verify(mockDispatchService, Mockito.after(100)).startDispatcher(Mockito.any());

    }

    @Test
    public void testDispatchEventsRegistersKevinProcessor() {

        when(mockDispatchService.isShutdown()).thenReturn(true).thenReturn(false); // Flip to handle conditional negation

        EventProcessor processor = new EventProcessor(new EventProcessorExecutorService());
        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, processor);
        queue.dispatchEvents(1, 1);

        assertEquals(1, processor.getProcessorsForEvent(KevinEvent.class).length, "Expected a processor for Kevin events");

    }

    @Test
    public void testDispatchEventsStopsProcessors() {

        when(mockDispatchService.isShutdown()).thenReturn(true).thenReturn(false); // Flip to handle conditional negation

        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, mockEventProcessor);
        queue.dispatchEvents(1, 1);

        verify(mockEventProcessor).stopProcessor();

    }

    @Test
    public void testDispachEventsProcessesEvent() {

        TestEvent event = new TestEvent();
        when(mockDispatchService.isShutdown()).thenReturn(false).thenReturn(true); // Run the dispatch loop only once

        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, mockEventProcessor);
        queue.queueEvent(event);
        assertTimeout(Duration.ofSeconds(5), () -> queue.dispatchEvents(), "Expected event to be sent for processing");

        verify(mockEventProcessor).processEvent(event);

    }

    @Test
    public void testDispachEventsShutsdownProcessorIfInterrupted() {

        when(mockDispatchService.isShutdown()).thenReturn(false).thenReturn(true); // Run the dispatch loop only once

        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, mockEventProcessor);
        assertTimeout(Duration.ofSeconds(5), () -> {
            Thread t = new Thread(() -> queue.dispatchEvents(3, 10));
            t.start();
            t.interrupt();
        });

        // Brief pause to allow dispatcher to shutdown
        verify(mockDispatchService, after(100)).stopDispatcher();

    }

    @Test
    public void testDispachEventsShutsdownProcessorAfterProcessingMaxKevins() {

        when(mockDispatchService.isShutdown()).thenReturn(false).thenReturn(true); // Run the dispatch loop only once

        EventProcessor processor = new EventProcessor(new EventProcessorExecutorService());
        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, processor);
        queue.queueEvent(new KevinEvent(System.currentTimeMillis(), 1, 0));    
        queue.dispatchEvents(3, 1);
        
        // Brief pause to allow dispatcher to shutdown
        verify(mockDispatchService, after(100)).stopDispatcher();

    }

    @Test
    public void testDispachEventsTimesOutWhenNoEvent() {

        when(mockDispatchService.isShutdown()).thenReturn(false).thenReturn(true); // Run the dispatch loop only once

        EngineEventQueue queue = new EngineEventQueue(mockDispatchService, mockEventProcessor);
        assertTimeout(Duration.ofSeconds(3), () -> queue.dispatchEvents(1, 2), "Expected resolution before timeout");
        verify(mockEventProcessor).processEvent(null);

    }
    
    private class TestEvent extends EngineEvent
    {
        TestEvent() {

            // Placeholder constructor

        }
    }
}
