package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

/**
 * Test cases for {@link PlayerStateChangeEvent}.
 * @author @essar
 */
public class PlayerStateChangeEventTests
{

    @Test
    public void testPlayerStateChangeEventIsCreatedWithPlayerAndStates() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        assertNotNull(new PlayerStateChangeEvent(player, oldState, newState), "Expected event to be instantiated");

    }
    
    @Test
    public void testPlayerStateChangeEventIsCreatedIfPlayerIsNull() {

        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        assertNotNull(new PlayerStateChangeEvent(null, oldState, newState), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerStateChangeEventIsCreatedIfOldStateIsNull() {

        Player player = new Player("test", "Test Player");
        PlayerState newState = PlayerState.FINISHED;
        assertNotNull(new PlayerStateChangeEvent(player, null, newState), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerStateChangeEventIsCreatedIfNewStateIsNull() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        assertNotNull(new PlayerStateChangeEvent(player, oldState, null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerStateChangeEventSetsNewState() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState, newState);
        assertEquals(newState, event.getNewState(), "Expected NewState to be set");

    }
    
    @Test
    public void testPlayerStateChangeEventSetsOldState() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState, newState);
        assertEquals(oldState, event.getOldState(), "Expected OldState to be set");

    }
    
    @Test
    public void testPlayerStateChangeEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState, newState);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }
    
    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        PlayerState oldState = PlayerState.INIT;
        PlayerState newState = PlayerState.FINISHED;
        PlayerStateChangeEvent event = new PlayerStateChangeEvent(player, oldState, newState);
        assertEquals("[PlayerStateChangeEvent] Test Player state changed: INIT -> FINISHED", event.toString(), "toString did not return expected value");

    }
}
