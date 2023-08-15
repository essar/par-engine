package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidPlayException extends EngineException
{
    public InvalidPlayException() {

        super();

    }

    public InvalidPlayException(String message) {

        super(message);

    }
    
}
