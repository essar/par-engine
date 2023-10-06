package uk.co.essarsoftware.par.engine.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class GameStateException extends RuntimeException
{
    public GameStateException() {

        super();

    }

    public GameStateException(String message) {

        super(message);

    }
}
