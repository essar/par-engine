package uk.co.essarsoftware.par.engine.core.app.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.DiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDrawAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PlayCardsAction;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class ActionsController
{
    private static final Logger _LOG = LoggerFactory.getLogger(ActionsController.class);

    private final ActionsService actions;

    @Autowired
    public ActionsController(final ActionsService actions) {

        this.actions = actions;

    }

    
    private static Mono<ActionResponse> handleEngineException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());
        return Mono.error(e);

    }
    

    @PostMapping(path = "/actions/discard")
    public Mono<ActionResponse> discard(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(actions::validateRequest)
            .map(DiscardAction::new)
            .map(a -> actions.runAction(actions::discard, a))
            .onErrorResume(ActionsController::handleEngineException);
            
    }

    @PostMapping(path = "/actions/pickupDiscard")
    public Mono<ActionResponse> pickupDiscard(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(PickupDiscardAction::new)
            .map(a -> actions.runAction(actions::pickupDiscard, a))
            .onErrorResume(ActionsController::handleEngineException);

    }

    @PostMapping(path = "/actions/pickupDraw")
    public Mono<ActionResponse> pickupDraw(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(PickupDrawAction::new)
            .map(a -> actions.runAction(actions::pickupDraw, a))
            .onErrorResume(ActionsController::handleEngineException);

    }

    @PostMapping(path = "/actions/playCards")
    public Mono<ActionResponse> playCards(@RequestBody final ActionRequest request) {

        return Mono.just(request)
            //.filter(ActionsService::validateRequest)
            .map(PlayCardsAction::new)
            .map(a -> actions.runAction(actions::playCards, a))
            .onErrorResume(ActionsController::handleEngineException);

    }
}
