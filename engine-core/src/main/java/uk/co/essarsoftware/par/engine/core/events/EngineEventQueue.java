package uk.co.essarsoftware.par.engine.core.events;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@Component
public class EngineEventQueue
{
    private static final Logger _LOG = LoggerFactory.getLogger(EngineEventQueue.class);
    private ArrayList<EventProcessorBinding<?>> eventProcessorBindings = new ArrayList<>();
    private LinkedBlockingQueue<EngineEvent> inboundQueue = new LinkedBlockingQueue<>(100);
    private LinkedBlockingQueue<EngineEvent> processedQueue = new LinkedBlockingQueue<>();
    private ExecutorService eventDispatcherService = Executors.newSingleThreadExecutor();
    private ExecutorService processorService = Executors.newCachedThreadPool();

    //@Autowired
    //private TaskFactory taskFactory;

    private void execBoundProcessors(final EngineEvent event) {

        eventProcessorBindings.stream()
            .filter(b -> b.getEventClass().isInstance(event))
            .filter(b -> b.shouldFire(event))
            .forEach(b -> {
                // Execute each processor in a new thread
                processorService.execute(() -> {
                    b.execute(event);
                });
            });
    }

    public Flux<EngineEvent> getEvents() {

        return Flux.create(sink -> {

            try {
                while (true) {

                    sink.next(processedQueue.take());

                }
            } catch (InterruptedException ie) {

                sink.error(ie);

            }
        });
    }

    @PostConstruct
    public void startDispatcher() {

        eventDispatcherService.submit(new EventDispatcherTask());
        _LOG.info("Started event dispatcher");
        
    }

    public boolean isClosed() {

        try {

            Thread t1 = new Thread(() -> {
                try {
                    eventDispatcherService.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException ie) {
                    _LOG.warn("Timed out waiting for eventDispatcherService");
                }
            });
            t1.start();
            Thread t2 = new Thread(() -> {
                try {
                    processorService.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException ie) {
                    _LOG.warn("Timed out waiting for processorService");
                }
            });
            t2.start();

            // Wait for termination
            t1.join(12000);
            t2.join(12000);
            
            return true;

        } catch(InterruptedException ie) {
            
            _LOG.warn("Timeout waiting for threads to join");
            return false;

        }
    }

    public void queueEvent(EngineEvent event) {

        inboundQueue.add(event);

    }

    public <E extends EngineEvent> void registerProcessor(Class<E> eventClz, Consumer<E> processorFunction) {

        eventProcessorBindings.add(new EventProcessorBinding<E>(eventClz, processorFunction));

    }

    public <E extends EngineEvent> void registerProcessor(Class<E> eventClz, Predicate<E> condition, Consumer<E> processorFunction) {

        eventProcessorBindings.add(new EventProcessorConditionalBinding<E>(eventClz, condition, processorFunction));

    }

    private class EventDispatcherTask implements Runnable
    {
        boolean running = true;

        @Override
        public void run() {

            int kevins = 0;
            long lastEventTimestamp = 0L;

            // Create stale game processor
            registerProcessor(KevinEvent.class, event -> {
                if (event.getCount() >= 10) {
                    _LOG.info("Stopping the game engine");
                    // Time to go
                    running = false;
                }
            });

            while (running) {

                _LOG.debug("Waiting for event");

                try {

                    EngineEvent event = inboundQueue.poll(30, TimeUnit.SECONDS);
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
                    if (!processedQueue.add(event)) {

                        _LOG.warn("Event could not be added to processed queue; dropping event");

                    }

                    _LOG.info(event.toString());

                } catch (InterruptedException ie) {

                    running = false;

                }
            }

            eventDispatcherService.shutdown();
            processorService.shutdown();

            _LOG.info("Event dispatcher stopped");

        }
    }

    private static class EventProcessorBinding<E extends EngineEvent>
    {
        private final Class<E> eventClz;
        private final Consumer<E> consumer;

        public EventProcessorBinding(Class<E> eventClz, Consumer<E> consumer) {

            this.eventClz = eventClz;
            this.consumer = consumer;

        }

        void execute(EngineEvent event) {

            getConsumer().accept(getEventClass().cast(event));

        }

        boolean shouldFire(EngineEvent event) {

            return true;

        }

        public Class<E> getEventClass() {

            return eventClz;

        }

        public Consumer<E> getConsumer() {

            return consumer;

        }
    }

    private static class EventProcessorConditionalBinding<E extends EngineEvent> extends EventProcessorBinding<E>
    {
        private final Predicate<E> condition;

        public EventProcessorConditionalBinding(Class<E> eventClz, Predicate<E> condition, Consumer<E> consumer) {

            super(eventClz, consumer);
            this.condition = condition;

        }

        @Override
        boolean shouldFire(EngineEvent event) {

            return getCondition().test(getEventClass().cast(event));

        }

        public Predicate<E> getCondition() {

            return  condition;

        }
    }
}
