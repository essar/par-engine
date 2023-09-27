package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link PlayerPlayCardsEvent}.
 * @author @essar
 */
public class PlayerPlayCardsEventTests
{

    @Test
    public void testPlayerPlayCardsIsCreatedWithPlayerAndPlay() {

        Player player = new Player("test", "Test Player");
        Play play = Mockito.mock(Play.class);
        assertNotNull(new PlayerPlayCardsEvent(player, play), "Expected event to be instantiated");

    }
    
    @Test
    public void testPlayerDiscardEventIsCreatedIfPlayerIsNull() {

        Play play = Mockito.mock(Play.class);
        assertNotNull(new PlayerPlayCardsEvent(null, play), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerDiscardEventIsCreatedIfPlayIsNull() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new PlayerPlayCardsEvent(player, null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testPlayerDiscardEventSetsPlayer() {

        Player player = new Player("test", "Test Player");
        Play play = Mockito.mock(Play.class);
        PlayerPlayCardsEvent event = new PlayerPlayCardsEvent(player, play);
        assertEquals(player, event.getPlayer(), "Expected Player to be set");

    }
    
    @Test
    public void testPlayerDiscardEventSetsCard() {

        Player player = new Player("test", "Test Player");
        Play play = Mockito.mock(Play.class);
        PlayerPlayCardsEvent event = new PlayerPlayCardsEvent(player, play);
        assertEquals(play, event.getPlay(), "Expected Play to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Player player = new Player("test", "Test Player");
        Play play = Mockito.mock(Play.class);
        when(play.getCards()).thenReturn(new Card[] { Card.as(Suit.CLUBS, Value.ACE), Card.as(Suit.DIAMONDS, Value.ACE), Card.as(Suit.HEARTS, Value.ACE)});

        PlayerPlayCardsEvent event = new PlayerPlayCardsEvent(player, play);
        assertEquals("[PlayerPlayCardsEvent] Test Player played 1C,1D,1H", event.toString(), "toString did not return expected value");

    }
}
