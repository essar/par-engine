package uk.co.essarsoftware.par.engine.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Wrapper class around an {@link ExecutorService} exposing a set of methods used by an {@link EventProcessor}.
 * @author @essar
 */
@Component
public class EventProcessorExecutorService
{

    private final ExecutorService processorService;

    /**
     * Instantiate a new EventProcessorService wrapped around the specified ExecutorService.
     * @param dispatcherService an ExecutorService that runs the dispatcher.
     */
    EventProcessorExecutorService(ExecutorService processorService) {

        this.processorService = processorService;

    }

    /**
     * Instantiate a new EventProcessorService using a {@link Executors#newCachedThreadPool()}.
     */
    public EventProcessorExecutorService() {

        this(Executors.newCachedThreadPool());

    }

    /**
     * Run a command leveraging this processor service.
     * @param command a command or process to execute.
     * @see ExecutorService#execute(Runnable)
     */
    public void execute(Runnable command) {

        processorService.execute(command);

    }

    /**
     * Check if the service has been shutdown.
     * @return {@code true} if the service has been shutdown and not processing new events, {@code false} if the service is still running.
     * @see ExecutorService#isShutdown()
     */
    public boolean isShutdown() {

        return processorService.isShutdown();

    }
    
    /**
     * Stop the dispatch service and stop processing new events.
     * @see ExecutorService#shutdown()
     */
    public void stopProcessor() {

        processorService.shutdown();

    }
}
