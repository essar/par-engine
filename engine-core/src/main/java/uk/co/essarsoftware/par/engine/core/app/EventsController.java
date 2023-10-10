package uk.co.essarsoftware.par.engine.core.app;

import java.util.function.Consumer;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import uk.co.essarsoftware.par.engine.EventProcessorBinding;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.events.EventProcessor;

/**
 * Controller class for event functions.
 * @author @essar
 */
@RestController
public class EventsController
{
    
    private final EventProcessor eventProcessor;

    /**
     * Instantiate new Events Controller.
     * @param eventProcessor game event processor for this game.
     */
    public EventsController(EventProcessor eventProcessor) {

        this.eventProcessor = eventProcessor;

    }

    /**
     * Generate a new event consumer function that passes events to a provided Sink.
     * @param emitter the FluxSink to pass events to.
     * @return a Consumer function.
     */
    static Consumer<EngineEvent> eventConsumer(final FluxSink<String> emitter) {

        return event -> emitter.next(event.toString());

    }

    /**
     * Get a stream of game events.
     * @return the events that have taken place and are taking place in the engine.
     * @throws InterruptedException if the event stream is interrupted.
     */
    @GetMapping(path = "/game/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() throws InterruptedException {

        // Create a new Flux that combines from historic events then new events
        return Flux.concat(
                // Stream over all previous events
                Flux.fromStream(eventProcessor.getEventStream())
                    .map(event -> event.toString()),
                // Create a new Flux that binds to the event processor
                Flux.create(emitter -> {
                    // Create binding
                    final EventProcessorBinding<? extends EngineEvent> binding = eventProcessor.registerProcessor(EngineEvent.class, eventConsumer(emitter));

                    // Clean up when client disconnects
                    emitter.onDispose(() -> eventProcessor.unregisterProcessor(binding));
                })
        );
    }
}
