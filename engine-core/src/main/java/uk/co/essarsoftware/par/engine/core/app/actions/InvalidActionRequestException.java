package uk.co.essarsoftware.par.engine.core.app.actions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.co.essarsoftware.par.engine.core.app.EngineException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidActionRequestException extends EngineException
{
    public InvalidActionRequestException() {

        super();

    }

    public InvalidActionRequestException(String message) {

        super(message);

    }
}
