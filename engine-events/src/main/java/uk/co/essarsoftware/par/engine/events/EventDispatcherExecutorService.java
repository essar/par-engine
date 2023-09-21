package uk.co.essarsoftware.par.engine.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Wrapper class exposing a set of methods used to control an event dispatch service.
 * @author @essar
 */
@Component
public class EventDispatcherExecutorService
{

    private final ExecutorService dispatcherService;

    /**
     * Instantiate a new EventDispatcherService wrapped around the specified ExecutorService.
     * @param dispatcherService an ExecutorService that runs the dispatcher.
     */
    EventDispatcherExecutorService(ExecutorService dispatcherService) {

        this.dispatcherService = dispatcherService;

    }

    /**
     * Instantiate a new EventDispatcherService using a {@link Executors#newSingleThreadExecutor()}.
     */
    public EventDispatcherExecutorService() {

        this(Executors.newSingleThreadExecutor());

    }

    /**
     * Check if the service has been shutdown.
     * @return {@code true} if the service has been shutdown and not dispatching new events, {@code false} if the service is still running.
     * @see ExecutorService#isShutdown()
     */
    public boolean isShutdown() {

        return dispatcherService.isShutdown();

    }

    /**
     * Start the dispatch service and run the provided dispatcher.
     * @param dispatcherProcess a dispatcher process to run with the service.
     * @see ExecutorService#submit()
     */
    public void startDispatcher(Runnable dispatcherProcess) {

        dispatcherService.submit(dispatcherProcess);

    }
    
    /**
     * Stop the dispatch service and stop dispatching new events.
     * @see ExecutorService#shutdown()
     */
    public void stopDispatcher() {

        dispatcherService.shutdown();

    }
}
