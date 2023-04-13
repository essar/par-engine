package uk.co.essarsoftware.par.engine.core.app.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.DiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDrawAction;

@RestController
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
    

    @PostMapping(path = "/actions/discard", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> discardJson(@RequestBody final ActionRequest<DiscardAction> request) {

        return Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::discard, rq))
            .onErrorResume(ActionsController::handleEngineException);
            
    }

    @PostMapping(path = "/actions/pickupDiscard", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> pickupDiscardJson(@RequestBody final ActionRequest<PickupDiscardAction> request) {

        return Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::pickupDiscard, rq))
            .onErrorResume(ActionsController::handleEngineException);

    }

    @PostMapping(path = "/actions/pickupDraw", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> pickupDrawJson(@RequestBody final ActionRequest<PickupDrawAction> request) {

        return Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::pickupDraw, rq))
            .onErrorResume(ActionsController::handleEngineException);

    }
}
