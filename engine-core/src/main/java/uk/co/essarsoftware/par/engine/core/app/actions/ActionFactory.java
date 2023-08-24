package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.ArrayList;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.engine.actions.Action;
import uk.co.essarsoftware.par.engine.actions.DiscardActionHandler;
import uk.co.essarsoftware.par.engine.actions.DiscardActionHandler.DiscardAction;
import uk.co.essarsoftware.par.engine.actions.PickupDiscardActionHandler;
import uk.co.essarsoftware.par.engine.actions.PickupDiscardActionHandler.PickupDiscardAction;
import uk.co.essarsoftware.par.engine.actions.PickupDrawActionHandler;
import uk.co.essarsoftware.par.engine.actions.PlayCardsActionHandler;
import uk.co.essarsoftware.par.engine.actions.PlayCardsActionHandler.PlayCardsAction;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.actions.PickupDrawActionHandler.PickupDrawAction;

@Component
public class ActionFactory
{

    private final DiscardActionHandler discardActionHandler;
    private final PickupDiscardActionHandler pickupDiscardActionHandler;
    private final PickupDrawActionHandler pickupDrawActionHandler;
    private final PlayCardsActionHandler playCardsActionHandler;

    @Autowired
    public ActionFactory(final DiscardActionHandler discardActionHandler, final PickupDiscardActionHandler pickupDiscardActionHandler, final PickupDrawActionHandler pickupDrawActionHandler, final PlayCardsActionHandler playCardsActionHandler) {

        this.discardActionHandler = discardActionHandler;
        this.pickupDiscardActionHandler = pickupDiscardActionHandler;
        this.pickupDrawActionHandler = pickupDrawActionHandler;
        this.playCardsActionHandler = playCardsActionHandler;

    }

    <E> void copyActionParameter(ActionRequest request, Action<?> action, String key, Class<E> clz, boolean required) {

        copyRequestParameter(request, action, key, clz, required, e -> e);

    }

    <E> void copyRequestParameter(ActionRequest request, Action<?> action, String key, Class<E> clz, boolean required, Function<E, ?> mappingFunction) {

        E value = request.getRequestParameter(key, clz);
        if (required && value == null) {

            throw new InvalidActionRequestException(String.format("Missing ActionRequest parameter: %s", key));

        }
        action.addActionParameter(key, mappingFunction.apply(value));

    }

    DiscardAction createDiscardAction(final ActionRequest request) {

        // Initialise request
        DiscardAction action = discardActionHandler.newAction(request.getRequestID(), request.getActionSequence(), request.getPlayerID());
        copyRequestParameter(request, action, "card", String.class, true, CardEncoder::asCard);
        return action;

    }

    PickupDiscardAction createPickupDiscardAction(final ActionRequest request) {

        // Initialise request
        PickupDiscardAction action = pickupDiscardActionHandler.newAction(request.getRequestID(), request.getActionSequence(), request.getPlayerID());
        return action;

    }

    PickupDrawAction createPickupDrawAction(final ActionRequest request) {

        // Initialise request
        PickupDrawAction action = pickupDrawActionHandler.newAction(request.getRequestID(), request.getActionSequence(), request.getPlayerID());
        return action;

    }

    PlayCardsAction createPlayCardsAction(final ActionRequest request) {

        // Initiaise request
        PlayCardsAction action = playCardsActionHandler.newAction(request.getRequestID(), request.getActionSequence(), request.getPlayerID());
        copyRequestParameter(request, action, "cards", ArrayList.class, true, CardEncoder::asCards);
        return action;

    }
}
