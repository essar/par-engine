package uk.co.essarsoftware.par.engine.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ActionOutOfSequenceException extends EngineException
{
    public ActionOutOfSequenceException() {

        super();

    }

    public ActionOutOfSequenceException(int providedSequence, int expectedSequence) {

        super(String.format("providedSeq=%04x; expectedSeq=%04x", providedSequence, expectedSequence));
        
    }
}