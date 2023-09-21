package uk.co.essarsoftware.par.engine.events;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class representing a queue of {@link EngineEvent}s that can be dispatched to listening processors.
 * Runs in separate threads allowing asynchronous and parallel processing of events.
 * @author @essar
 */
@Component
public class EngineEventQueue
{
    private static final Logger _LOG = LoggerFactory.getLogger(EngineEventQueue.class);

    private final EventProcessor queueProcessor;
    private final LinkedBlockingQueue<EngineEvent> inboundQueue;
    private final EventDispatcherExecutorService eventDispatcherService;
     

    /**
     * Instantiate a new EngineEventQueue with the specified references.
     * @param eventDispatcherService service that wraps around an {@code ExecutorService} for dispatching events.
     * @param queueProcessor service that processes events sent to the event queue.
     */
    @Autowired
    public EngineEventQueue(final EventDispatcherExecutorService eventDispatcherService, final EventProcessor queueProcessor) {

        inboundQueue = new LinkedBlockingQueue<>(100);
        
        this.eventDispatcherService = eventDispatcherService;
        this.queueProcessor = queueProcessor;

    }

    /**
     * Dispatch events in the queue using default parameters.
     */
    void dispatchEvents() {

        dispatchEvents(30, 10);

    }

    /**
     * Dispatch events in the queue passing parameters.
     * @param timeoutSeconds number of seconds to wait for an event before raising a timeout event.
     * @param maxKevins the maximum number of timeout events before terminating the game.
     */
    void dispatchEvents(final int timeoutSeconds, final int maxKevins) {

        // Create stale game processor
        queueProcessor.registerProcessor(KevinEvent.class, event -> {
            if (event.getCount() >= maxKevins) {
                _LOG.info("Stopping the game engine");
                // Time to go
                eventDispatcherService.stopDispatcher();
            }
        });

        _LOG.info("Started dispatching events");

        while (!eventDispatcherService.isShutdown()) {

            _LOG.debug("Waiting for event");

            try {

                EngineEvent event = inboundQueue.poll(timeoutSeconds, TimeUnit.SECONDS);
                _LOG.debug("Read {} from the queue", (event == null ? "null" : event.getClass().getSimpleName()));

                queueProcessor.processEvent(event);

            } catch (InterruptedException ie) {

                // Stop processing any more events
                eventDispatcherService.stopDispatcher();

            }
        }

        // Clean up the processor service as well
        queueProcessor.stopProcessor();

        _LOG.info("Stopped dispatching events");

    }

    /**
     * Add an event to the queue.
     * @param event an EngineEvent created within the game engine.
     */
    public void queueEvent(EngineEvent event) {

        inboundQueue.add(event);

    }

    /**
     * Start the event dispatcher.
     */
    @PostConstruct
    public void startDispatcher() {

        eventDispatcherService.startDispatcher(this::dispatchEvents);
        
    }
}
