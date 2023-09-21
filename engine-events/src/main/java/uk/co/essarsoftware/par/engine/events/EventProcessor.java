package uk.co.essarsoftware.par.engine.events;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class responsible for processing events received in the {@link EngineEventQueue}.
 * Events are matched against registered event processor bindings and executed asynchronously.
 * @author @essar
 */
@Component
public class EventProcessor
{

    private static final Logger _LOG = LoggerFactory.getLogger(EventProcessor.class);

    private final ArrayList<EventProcessorBinding<?>> eventProcessorBindings;
    private final LinkedBlockingQueue<EngineEvent> processedQueue;
    private final EventProcessorExecutorService executorService;

    private int kevins = 0;
    private long lastEventTimestamp = 0L;

    /**
     * Instantiate a new EventProcessor that references the specified {@code EventProcessorExecutorServiceWrapper}.
     * @param executorService service exposing {@link ExecutorService} methods for this object.
     */
    @Autowired
    public EventProcessor(final EventProcessorExecutorService executorService) {

        this.executorService = executorService;

        eventProcessorBindings = new ArrayList<>();
        processedQueue = new LinkedBlockingQueue<>();

    }

    /**
     * Execute all bound processors against an event. Events are executed asynchronously by the executorService.
     * @param event the event to process.
     */
    private void execBoundProcessors(final EngineEvent event) {

        eventProcessorBindings.stream()
            .filter(b -> b.getEventClass().isInstance(event))
            .filter(b -> b.shouldFire(event))
            .forEach(b -> {
                // Execute each processor in a new thread
                executorService.execute(() -> {
                    b.execute(event);
                });
            });
    }

    /**
     * Get the number of timeout events raised since the last event process.
     * @return a count of timeout events.
     */
    int getKevinCount() {

        return kevins;

    }

    /**
     * Get the timestamp since the last event process.
     * @return the epoch milliseconds of the last event.
     */
    long getLastEventTimestamp() {

        return lastEventTimestamp;

    }

    /**
     * Get an array of processors registered for a given {@link EngineEvent} class.
     * @param eventClz the class of event to obtain processors for.
     * @return an array of {@link Consumer}s, registered against the provided event class.
     */
    Consumer<?>[] getProcessorsForEvent(Class<? extends EngineEvent> eventClz) {

        return eventProcessorBindings.stream()
            .filter(b -> b.getEventClass().isAssignableFrom(eventClz))
            .map(b -> b.getConsumer())
            .toArray(Consumer[]::new);

    }

    /**
     * Get a stream of previously processed events.
     * @return a Stream containing all processed events.
     */
    public Stream<EngineEvent> getEventStream() {

        return processedQueue.stream();

    }

    /**
     * Process a provided event. If {@code null} is passed, it is assumed to be a timeout so a {@link KevinEvent}
     * is automatically created and processed.
     * @param event the EnginEvent to process.
     */
    public void processEvent(EngineEvent event) {

        if (event == null) {

            // Not received an event, increment kevin count and create event
            event = new KevinEvent(++kevins, lastEventTimestamp);

        } else {

            // Set event dispatch time
            event.setEventDispatchedTimestamp(System.currentTimeMillis());

            // Reset kevin count
            kevins = 0;

            // Record event timestamp
            lastEventTimestamp = event.getEventCreatedTimestamp();

        }

        _LOG.debug("Received event: {}", event);

        // Send to interested processors
        execBoundProcessors(event);

        // Add to processsed queue
        processedQueue.add(event);

        _LOG.info(event.toString());

    }

    /**
     * Register a processor function for a given EngineEvent class.
     * @param <E> subclass of {@link EngineEvent}.
     * @param eventClz the class of event to process with this function.
     * @param processorFunction the function to execute.
     */
    public <E extends EngineEvent> void registerProcessor(Class<E> eventClz, Consumer<E> processorFunction) {

        eventProcessorBindings.add(new EventProcessorBinding<E>(eventClz, processorFunction));

    }

    /**
     * Register a processor function for a given EngineEvent class, gated by a given predicate.
     * @param <E> subclass of {@link EngineEvent}.
     * @param eventClz the class of event to process with this function.
     * @param condition the predicate condition to determine if this function should be executed.
     * @param processorFunction the function to execute.
     */
    public <E extends EngineEvent> void registerProcessor(Class<E> eventClz, Predicate<E> condition, Consumer<E> processorFunction) {

        eventProcessorBindings.add(new EventProcessorConditionalBinding<E>(eventClz, condition, processorFunction));

    }

    /**
     * Stop the underlying processor execution service.
     */
    public void stopProcessor() {

        executorService.stopProcessor();

    }

    /**
     * Represents a binding of a processor function and a specified class.
     * @author @essar
     * @param <E> subclass of {@link EngineEvent}.
     */
    private static class EventProcessorBinding<E extends EngineEvent>
    {
        private final Class<E> eventClz;
        private final Consumer<E> consumer;

        /**
         * Instantiate a new binding between a specified class and processor function.
         * @param eventClz the class of event to process with this function.
         * @param consumer the function to execute.
         */
        public EventProcessorBinding(Class<E> eventClz, Consumer<E> consumer) {

            this.eventClz = eventClz;
            this.consumer = consumer;

        }

        /**
         * Execute the bound function against a provided event.
         * @param event the event to execute against this function.
         */
        void execute(EngineEvent event) {

            getConsumer().accept(getEventClass().cast(event));

        }

        /**
         * Specify if this function should execute. Always returns {@code true}.
         * @param event the event to execute against this function.
         * @return boolean indicating if the process functiom should be executed.
         */
        boolean shouldFire(EngineEvent event) {

            return true;

        }

        /**
         * Get the class of event this binding applies to.
         * @return a subclass of {@link EngineEvent}.
         */
        public Class<E> getEventClass() {

            return eventClz;

        }

        /**
         * Get the bound processor function.
         * @return a single-argument function bound to the event class.
         */
        public Consumer<E> getConsumer() {

            return consumer;

        }
    }

    /**
     * Represents a binding of a processor function and a specified class that executes based on a specified predicate.
     * @author @essar
     * @param <E> subclass of {@link EngineEvent}.
     */
    private static class EventProcessorConditionalBinding<E extends EngineEvent> extends EventProcessorBinding<E>
    {
        private final Predicate<E> condition;

        /**
         * Instantiate a new binding between a specified class and processor function.
         * @param eventClz the class of event to process with this function.
         * @param condition a predicate that determines if this binding should execute for a given event.
         * @param consumer the function to execute.
         */
        public EventProcessorConditionalBinding(Class<E> eventClz, Predicate<E> condition, Consumer<E> consumer) {

            super(eventClz, consumer);
            this.condition = condition;

        }

        /**
         * Specify if this function should execute.
         * @param event the event to execute against this function.
         * @return boolean indicating if the process functiom should be executed.
         */
        @Override
        boolean shouldFire(EngineEvent event) {

            return getCondition().test(getEventClass().cast(event));

        }

        /**
         * Get the predicate from this conditional binding.
         * @return the predicate function.
         */
        public Predicate<E> getCondition() {

            return  condition;

        }
    }
}
