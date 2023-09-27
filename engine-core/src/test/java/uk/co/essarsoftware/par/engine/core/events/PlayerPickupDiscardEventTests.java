package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link PlayerPickupDiscardEvent}.
 * @author @essar
 */
public class PlayerPickupDiscardEventTests
{

    @Test
    public void testPlayerPickupDiscardEventIsCreatedWithPlayerAndCard() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertNotNull(new PlayerPickupDiscardEvent(player, card), "Expected event to be instantiated");

    }
    
    @Test
    public void testPlayerPickupDiscardEventIsCreatedIfPlayerIsNull() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertNotNull(new PlayerPickupDiscardEvent(null, card), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerPickupDiscardEventIsCreatedIfCardIsNull() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new PlayerPickupDiscardEvent(player, null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerPickupDiscardEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerPickupDiscardEvent event = new PlayerPickupDiscardEvent(player, card);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }
    
    @Test
    public void testPlayerPickupDiscardEventSetsCard() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerPickupDiscardEvent event = new PlayerPickupDiscardEvent(player, card);
        assertEquals(card, event.getCard(), "Expected Card to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerPickupDiscardEvent event = new PlayerPickupDiscardEvent(player, card);
        assertEquals("[PlayerPickupDiscardEvent] Test Player picked up 1C from the discard pile", event.toString(), "toString did not return expected value");

    }
}
