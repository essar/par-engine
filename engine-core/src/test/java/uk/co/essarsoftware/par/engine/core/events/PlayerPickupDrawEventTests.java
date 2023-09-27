package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link PlayerPickupDrawEvent}.
 * @author @essar
 */
public class PlayerPickupDrawEventTests
{

    @Test
    public void testPlayerPickupDrawEventIsCreatedWithPlayer() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new PlayerPickupDrawEvent(player), "Expected event to be instantiated");

    }
    
    @Test
    public void testPlayerPickupDrawEventIsCreatedIfPlayerIsNull() {

        assertNotNull(new PlayerPickupDrawEvent(null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerPickupDrawEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        PlayerPickupDrawEvent event = new PlayerPickupDrawEvent(player);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        PlayerPickupDrawEvent event = new PlayerPickupDrawEvent(player);
        assertEquals("[PlayerPickupDrawEvent] Test Player picked up from the draw pile", event.toString(), "toString did not return expected value");

    }
}
