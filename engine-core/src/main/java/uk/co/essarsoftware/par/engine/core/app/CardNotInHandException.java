package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class CardNotInHandException extends EngineException
{
    public CardNotInHandException() {

        super();

    }

    public CardNotInHandException(String message) {

        super(message);

    }
    
}
