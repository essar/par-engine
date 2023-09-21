package uk.co.essarsoftware.par.engine.core.app.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class ActionsController
{
    private static final Logger _LOG = LoggerFactory.getLogger(ActionsController.class);

    private final ActionFactory actionFactory;
    private final ActionsService actions;

    public ActionsController(final ActionsService actions, final ActionFactory actionFactory) {

        this.actions = actions;
        this.actionFactory = actionFactory;

    }

    
    private static Mono<ActionResponse> handleEngineException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());
        return Mono.error(e);

    }
    

    @PostMapping(path = "/actions/discard")
    public Mono<ActionResponse> discard(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(actions::validateRequest)
            .map(actionFactory::createDiscardAction)
            .map(actions::discard)
            .map(actions::createActionResponse)
            .onErrorResume(ActionsController::handleEngineException);
            
    }

    @PostMapping(path = "/actions/pickupDiscard")
    public Mono<ActionResponse> pickupDiscard(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(actionFactory::createPickupDiscardAction)
            .map(actions::pickupDiscard)
            .map(actions::createActionResponse)
            .onErrorResume(ActionsController::handleEngineException);

    }

    @PostMapping(path = "/actions/pickupDraw")
    public Mono<ActionResponse> pickupDraw(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(actionFactory::createPickupDrawAction)
            .map(actions::pickupDraw)
            .map(actions::createActionResponse)
            .onErrorResume(ActionsController::handleEngineException);

    }

    @PostMapping(path = "/actions/playCards")
    public Mono<ActionResponse> playCards(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(actionFactory::createPlayCardsAction)
            .map(actions::playCards)
            .map(actions::createActionResponse)
            .onErrorResume(ActionsController::handleEngineException);

    }
}
