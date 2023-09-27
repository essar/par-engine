package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link PlayerDiscardEvent}.
 * @author @essar
 */
public class PlayerDiscardEventTests
{

    @Test
    public void testPlayerDiscardEventIsCreatedWithPlayerAndCard() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertNotNull(new PlayerDiscardEvent(player, card), "Expected event to be instantiated");

    }
    
    @Test
    public void testPlayerDiscardEventIsCreatedIfPlayerIsNull() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertNotNull(new PlayerDiscardEvent(null, card), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerDiscardEventIsCreatedIfCardIsNull() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new PlayerDiscardEvent(player, null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerDiscardEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerDiscardEvent event = new PlayerDiscardEvent(player, card);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }
    
    @Test
    public void testPlayerDiscardEventSetsCard() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerDiscardEvent event = new PlayerDiscardEvent(player, card);
        assertEquals(card, event.getCard(), "Expected Card to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        PlayerDiscardEvent event = new PlayerDiscardEvent(player, card);
        assertEquals("[PlayerDiscardEvent] Test Player discarded 1C", event.toString(), "toString did not return expected value");

    }
}
