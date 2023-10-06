package uk.co.essarsoftware.par.engine.core.exceptions;

public class EngineException extends RuntimeException
{
    public EngineException() {

        super();

    }

    public EngineException(String message) {

        super(message);

    }

    public EngineException(Throwable cause) {

        super(cause);

    }

    public EngineException(String message, Throwable cause) {

        super(message, cause);

    }
}
