package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.events.EventProcessor;

/**
 * Test cases for {@code EventsController}.
 * @author @essar
 */
@ExtendWith(SpringExtension.class)
public class EventsControllerTests
{

    @MockBean
    private EventProcessor eventProcessor;
    
    private EventsController underTest;

    @BeforeEach
    public void initEventsController() {

        underTest = new EventsController(eventProcessor);
        
    }

    @Test
    public void testEventConsumerCallsEmitter() {

        EngineEvent testEvent = mock(EngineEvent.class);
        when(testEvent.toString()).thenReturn("testEvent");
        FluxSink<String> testSink = mock(TestSink.class);

        EventsController.eventConsumer(testSink).accept(testEvent);
        verify(testSink).next("testEvent");

    }

    @Test
    public void testStreamEventsReturnsProcessedEvents() throws InterruptedException {

        EngineEvent testEvent = mock(EngineEvent.class);
        when(testEvent.toString()).thenReturn("testEvent");
        when(eventProcessor.getEventStream()).thenReturn(Stream.of(testEvent));

        String response = underTest.streamEvents().blockFirst();
        assertEquals("testEvent", response, "Response should contain TestEvent from processor stream");
        
    }

    @Test
    public void testStreamEventsRegistersEventProcessor() throws InterruptedException {

        Flux<String> stream = underTest.streamEvents();
        assertThrows(IllegalStateException.class, () -> stream.blockFirst(Duration.ofMillis(100)), "Expected stream to return empty");
        verify(eventProcessor).registerProcessor(any(), any());
        
    }

    @Test
    public void testStreamEventsUnregistersEventProcessor() throws InterruptedException {

        Flux<String> stream = underTest.streamEvents();
        assertThrows(IllegalStateException.class, () -> stream.blockFirst(Duration.ofMillis(100)), "Expected stream to return empty");
        verify(eventProcessor).unregisterProcessor(any());
        
    }

    private static interface TestSink extends FluxSink<String>
    {
        // Empty interface
    }
}
