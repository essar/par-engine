package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnknownPlayerException extends EngineException
{
    public UnknownPlayerException() {

        super();

    }

    public UnknownPlayerException(String message) {

        super(message);

    }
}
