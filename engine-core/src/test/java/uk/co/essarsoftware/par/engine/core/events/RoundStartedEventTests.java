package uk.co.essarsoftware.par.engine.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link RoundStartedEvent}.
 * @author @essar
 */
public class RoundStartedEventTests
{

    @Test
    public void testRoundStartedEventIsCreatedWithRoundAndPlayer() {

        Round round = Round.START;
        Player player = new Player("test", "Test Player");
        assertNotNull(new RoundStartedEvent(round, player), "Expected event to be instantiated");

    }
    
    @Test
    public void testRoundStartedEventIsCreatedIfRoundIsNull() {

        Player player = new Player("test", "Test Player");
        assertNotNull(new RoundStartedEvent(null, player), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testRoundStartedEventIsCreatedIfPlayerIsNull() {

        Round round = Round.START;
        assertNotNull(new RoundStartedEvent(round, null), "Expected event to be instantiated");
        
    }
    
    @Test
    public void testRoundStartedEventSetsCurrentPlayer() {

        Round round = Round.START;
        Player player = new Player("test", "Test Player");
        RoundStartedEvent event = new RoundStartedEvent(round, player);
        assertEquals(player, event.getCurrentPlayer(), "Expected CurrentPlayer to be set");

    }
    
    @Test
    public void testRoundStartedEventSetsRound() {

        Round round = Round.START;
        Player player = new Player("test", "Test Player");
        RoundStartedEvent event = new RoundStartedEvent(round, player);
        assertEquals(round, event.getRound(), "Expected Round to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        Round round = Round.START;
        Player player = new Player("test", "Test Player");
        RoundStartedEvent event = new RoundStartedEvent(round, player);
        assertEquals("[RoundStartedEvent] Round START started; Test Player is the first player", event.toString(), "toString did not return expected value");

    }
}
