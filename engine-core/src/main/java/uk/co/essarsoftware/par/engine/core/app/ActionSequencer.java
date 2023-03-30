package uk.co.essarsoftware.par.engine.core.app;

import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class ActionSequencer
{

    private int sequenceNo = 0;


    public int getSequence() {

        return sequenceNo;

    }

    public synchronized <E extends ActionRequest, F extends ActionResponse> ActionResponse runAction(Function<E, F> actionFunction, E request) {

        if (request.getActionSequence() != this.sequenceNo) {

            throw new ActionOutOfSequenceException();

        }
        F response = actionFunction.apply(request);

        // Increment sequence if action is successful
        this.sequenceNo ++;

        // Inject next sequence number into response
        response.setNextActionSequence(sequenceNo);

        return response;

    }
}
