package uk.co.essarsoftware.par.engine;

import java.util.function.Consumer;

import uk.co.essarsoftware.par.engine.events.EngineEvent;

/**
 * Represents a binding of a processor function and a specified class.
 * @author @essar
 * @param <E> subclass of {@link EngineEvent}.
 */
public interface EventProcessorBinding <E extends EngineEvent>
{

    /**
     * Execute the bound function against a provided event.
     * @param event the event to execute against this function.
     */
    void execute(EngineEvent event);

    /**
     * Specify if this function should execute. Always returns {@code true}.
     * @param event the event to execute against this function.
     * @return boolean indicating if the process functiom should be executed.
     */
    boolean shouldFire(EngineEvent event);

    /**
     * Get the class of event this binding applies to.
     * @return a subclass of {@link EngineEvent}.
     */
    public Class<E> getEventClass();

    /**
     * Get the bound processor function.
     * @return a single-argument function bound to the event class.
     */
    public Consumer<E> getConsumer();
    
}
