package uk.co.essarsoftware.par.engine.events;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.engine.EventProcessorBinding;

public class EventProcessorTests
{

    private EventProcessorExecutorService mockProcessorService;

    @BeforeEach
    public void mockEventProcessor() {

        mockProcessorService = Mockito.mock(EventProcessorExecutorService.class);

    }

    @Test
    public void testGetEventStreamReturnsEmptyStreamForNewProcessor() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        assertEquals(0, processor.getEventStream().count(), "Expected stream with zero elements");

    }

    @Test
    public void testGetKevinsReturnsZeroForNewProcessor() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        assertEquals(0, processor.getKevinCount(), "Processor should start with no kevins");

    }

    @Test
    public void testGetLastEventTimestampReturnsZeroForNewProcessor() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        assertEquals(0, processor.getLastEventTimestamp(), "Processor should have zero for last event when new");

    }

    @Test
    public void testProcessEventCallsEventConsumer() {

        TestConsumer mockConsumer = mock(TestConsumer.class);
        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(new EventProcessorExecutorService());
        processor.registerProcessor(TestEvent.class, mockConsumer);
        processor.processEvent(event);
        // Short delay to allow threads to execute
        verify(mockConsumer, after(100)).accept(event);

    }

    @Test
    public void testProcessEventDoesExecuteMatchingEventConsumer() {

        TestConsumer mockConsumer = mock(TestConsumer.class);
        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, e -> true, mockConsumer);
        processor.processEvent(event);
        verify(mockProcessorService).execute(any());

    }

    @Test
    public void testProcessEventDoesNotExecuteConditionalEventConsumer() {

        TestConsumer mockConsumer = mock(TestConsumer.class);
        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, e -> false, mockConsumer);
        processor.processEvent(event);
        verify(mockProcessorService, never()).execute(any());

    }

    @Test
    public void testProcessEventDoesNotExecuteForAlternateClass() {

        AlternateTestConsumer alternateConsumer = mock(AlternateTestConsumer.class);
        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(AlternateTestEvent.class, e -> true, alternateConsumer);
        processor.processEvent(event);
        verify(mockProcessorService, never()).execute(any());

    }

    @Test
    public void testProcessEventResetsKevinCount() {

        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.processEvent(event);
        assertEquals(0, processor.getKevinCount(), "Expected kevin count to be reset");

    }

    @Test
    public void testProcessEventSetsLastEventTimestamp() {

        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.processEvent(event);
        assertEquals(event.getEventCreatedTimestamp(), processor.getLastEventTimestamp(), "Expected last event timestamp to match event created timestamp");

    }

    @Test
    public void testProcessEventSetsDispatchedTimestamp() {

        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.processEvent(event);
        assertTrue(event.getEventDispatchedTimestamp() > 0, "Expected event dispatched timestamp to be set");

    }

    @Test
    public void testProcessEventAddsEventToProcessedQueue() {

        TestEvent event = new TestEvent();

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.processEvent(event);
        assertEquals(event, processor.getEventStream().findFirst().get(),"Expected event to be on processed queue");

    }

    @Test
    public void testProcessEventIncrementsKevinCountForNullEvent() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        int initialKevinCount = processor.getKevinCount();
        processor.processEvent(null);
        assertEquals(initialKevinCount + 1, processor.getKevinCount(), "Expected kevin count to be incremented");

    }

    @Test
    public void testProcessEventCreatesKevinEventForNullEvent() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.processEvent(null);
        assertEquals(KevinEvent.class, processor.getEventStream().findFirst().get().getClass(), "Expected kevin event to be on processed queue");

    }

    @Test
    public void testRegisterProcessorCreatesBindingForConsumer() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        EventProcessorBinding<TestEvent> binding = processor.registerProcessor(TestEvent.class, mockConsumer);
        assertEquals(mockConsumer, binding.getConsumer(),"Expected binding to be attached to our consumer");
        
    }

    @Test
    public void testRegisterProcessorCreatesBindingForEventClass() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        EventProcessorBinding<TestEvent> binding = processor.registerProcessor(TestEvent.class, mockConsumer);
        assertEquals(TestEvent.class, binding.getEventClass(),"Expected binding to be for our event class");
        
    }

    @Test
    public void testRegisterProcessorAddsBindingToList() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, mockConsumer);
        assertEquals(mockConsumer, processor.getProcessorsForEvent(TestEvent.class)[0], "Expected list to contain processor for TestEvent class");

    }

    @Test
    public void testRegisterProcessorDoesNotAddBindingToListForSuperclassEvent() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, mockConsumer);
        assertEquals(0, processor.getProcessorsForEvent(EngineEvent.class).length, "Expected list to be empty for EngineEvent class");

    }

    @Test
    public void testRegisterProcessorDoesNotAddBindingToListForSubclassEvent() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, mockConsumer);
        assertEquals(mockConsumer, processor.getProcessorsForEvent(SubTestEvent.class)[0], "Expected list to contain processor for SubTestEvent class");

    }

    @Test
    public void testRegisterProcessorCreatesBindingForConsumerForConditionalBinding() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        EventProcessorBinding<TestEvent> binding = processor.registerProcessor(TestEvent.class, e -> e.getClass() == TestEvent.class, mockConsumer);
        assertEquals(mockConsumer, binding.getConsumer(),"Expected binding to be attached to our consumer");
        
    }

    @Test
    public void testRegisterProcessorCreatesBindingForEventClassForConditionalBinding() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        EventProcessorBinding<TestEvent> binding = processor.registerProcessor(TestEvent.class, e -> e.getClass() == TestEvent.class, mockConsumer);
        assertEquals(TestEvent.class, binding.getEventClass(),"Expected binding to be for our event class");
        
    }

    @Test
    public void testRegisterProcessorAddsBindingToListForConditionalBinding() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.registerProcessor(TestEvent.class, e -> e.getClass() == TestEvent.class, mockConsumer);
        assertEquals(mockConsumer, processor.getProcessorsForEvent(TestEvent.class)[0], "Expected list to contain processor for TestEvent class");

    }

    @Test
    public void testStopProcessorCallsShutdown() {

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.stopProcessor();
        verify(mockProcessorService).stopProcessor();

    }

    @Test
    public void testUnregisterProcessorRemovesProcessor() {

        TestConsumer mockConsumer = mock(TestConsumer.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        EventProcessorBinding<TestEvent> binding = processor.registerProcessor(TestEvent.class, mockConsumer);
        processor.unregisterProcessor(binding);
        assertArrayEquals(new Consumer[0], processor.getProcessorsForEvent(TestEvent.class), "Expected list to be empty for TestEvent class");

    }

    @Test
    public void testUnregisterProcessorDoesNothingForUnknownProcessor() {

        EventProcessorBinding<TestEvent> binding = mock(TestEventProcessorBinding.class);

        EventProcessor processor = new EventProcessor(mockProcessorService);
        processor.unregisterProcessor(binding);
        assertArrayEquals(new Consumer[0], processor.getProcessorsForEvent(TestEvent.class), "Expected list to be empty for TestEvent class");

    }

    private interface TestConsumer extends Consumer<TestEvent>
    {
        // Empty interface
    }

    private interface AlternateTestConsumer extends Consumer<AlternateTestEvent>
    {
        // Empty interface
    }

    private static class TestEvent extends EngineEvent
    {
        // Empty class
    }

    private static class AlternateTestEvent extends EngineEvent
    {
        // Empty class
    }

    private static class SubTestEvent extends TestEvent
    {
        // Empty class
    }

    private static interface TestEventProcessorBinding extends EventProcessorBinding<TestEvent>
    {
        // Empty interface
    }
}
