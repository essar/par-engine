package uk.co.essarsoftware.par.engine.core.events;

public abstract class EngineTask<E>
{

    public abstract E processEvent(EngineEvent event);
    
}
