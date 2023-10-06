package uk.co.essarsoftware.par.engine.core.app.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.co.essarsoftware.par.engine.actions.Action;
import uk.co.essarsoftware.par.engine.core.exceptions.ActionOutOfSequenceException;

@Component
class ActionSequencer
{
    private static Logger _LOGGER = LoggerFactory.getLogger(ActionSequencer.class);
    private int sequenceNum = 1;

    int getSequence() {

        return sequenceNum;

    }

    int nextSequence() {

        return ++sequenceNum;
        
    }

    boolean validateSequence(Action<?> action) {

        _LOGGER.debug("Validating sequence number: received {}, expecting {}", action.getActionSequence(), sequenceNum);
        if (action.getActionSequence() == sequenceNum) {

            return true;

        }
        throw new ActionOutOfSequenceException(action.getActionSequence(), sequenceNum);
    
    }
}
