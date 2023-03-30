package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotCurrentPlayerException extends RuntimeException
{

    public NotCurrentPlayerException() {

        super();

    }

    public NotCurrentPlayerException(String message) {

        super(message);

    }
    
}
