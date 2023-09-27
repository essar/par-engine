package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link NextPlayerEvent}.
 * @author @essar
 */
public class NextPlayerEventTests
{

    @Test
    public void testNextPlayerEventIsCreatedWithPlayer() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new NextPlayerEvent(player), "Expected event to be instantiated");

    }
    
    @Test
    public void testNextPlayerEventIsCreatedIfPlayerIsNull() {

        assertNotNull(new NextPlayerEvent(null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testNextPlayerEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        NextPlayerEvent event = new NextPlayerEvent(player);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }
    
    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        NextPlayerEvent event = new NextPlayerEvent(player);
        assertEquals("[NextPlayerEvent] Test Player now the active player", event.toString(), "toString did not return expected value");

    }
}
