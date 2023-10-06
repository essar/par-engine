package uk.co.essarsoftware.par.engine.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class InvalidPlayerStateException extends RuntimeException
{
    public InvalidPlayerStateException() {

        super();

    }

    public InvalidPlayerStateException(String message) {

        super(message);

    }
    
}
