package uk.co.essarsoftware.par.engine.players;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;

public class PlayerTests
{

    @Test
    public void testCreatedPlayerHasID() {

        Player player = new Player("test", "Test Player");
        assertEquals("test", player.getPlayerID(), "Expected Player to have playerID");

    }
    
    @Test
    public void testCreatedPlayerHasName() {

        Player player = new Player("test", "Test Player");
        assertEquals("Test Player", player.getPlayerName(), "Expected Player to have playerName");
        
    }
    
    @Test
    public void testCreatedPlayerHasHand() {

        Player player = new Player("test", "Test Player");
        assertNotNull(player.getHand(), "Expected Player to have Hand");
        
    }
    
    @Test
    public void testCreatedPlayerHasZeroHandSize() {

        Player player = new Player("test", "Test Player");
        assertEquals(0, player.getHandSize(), "Expected Player to have zero Hand size");
        
    }
    
    @Test
    public void testCreatedPlayerHasPenaltyCardObject() {

        Player player = new Player("test", "Test Player");
        assertNotNull(player.getPenaltyCard(), "Expected Player to have PenaltyCard object");
        
    }
    
    @Test
    public void testCreatedPlayerHasEmptyPenaltyCard() {

        Player player = new Player("test", "Test Player");
        assertNull(player.getPenaltyCard().getPenaltyCard(), "Expected Player to have PenaltyCard empty object");
        
    }
    
    @Test
    public void testCreatedPlayerHasNoPenaltyCard() {

        Player player = new Player("test", "Test Player");
        assertFalse(player.hasPenaltyCard(), "Expected Player to have no PenaltyCard");
        
    }
    
    @Test
    public void testCreatedPlayerIsNotDown() {

        Player player = new Player("test", "Test Player");
        assertFalse(player.isDown(), "Expected Player to not be down");
        
    }
    
    @Test
    public void testCreatedPlayerInInitState() {

        Player player = new Player("test", "Test Player");
        assertEquals(PlayerState.INIT, player.getPlayerState(), "Expected Player to be in INIT state");
        
    }

    @Test
    public void testGetFlagsNoPenaltyCardNotDown() {

        Player player = new Player("test", "Test Player");
        assertArrayEquals(new String[0], player.getFlags(), "Expected an empty String array");

    }

    @Test
    public void testGetFlagsNoPenaltyCardDown() {

        Player player = new Player("test", "Test Player");
        player.setPlayerDown();
        assertArrayEquals(new String[] { "DOWN" }, player.getFlags(), "Expected a String array containing \"DOWN\"");

    }

    @Test
    public void testGetFlagsWithPenaltyCardNotDown() {

        Player player = new Player("test", "Test Player");
        player.getPenaltyCard().setPenaltyCard(Card.as(Suit.CLUBS, Value.ACE));
        assertArrayEquals(new String[] { "PC" }, player.getFlags(), "Expected a String array containing \"PC\\");
        
    }

    @Test
    public void testGetFlagsWithPenaltyCardDown() {

        Player player = new Player("test", "Test Player");
        player.setPlayerDown();
        player.getPenaltyCard().setPenaltyCard(Card.as(Suit.CLUBS, Value.ACE));
        assertArrayEquals(new String[] { "PC", "DOWN" }, player.getFlags(), "Expected a String array containing \"PC\" and \"DOWN\"");
        
    }

    @Test
    public void testGetHandSizeIncreasesAfterCardAdded() {

        Player player = new Player("test", "Test Player");
        player.getHand().addCard(Card.as(Suit.CLUBS, Value.ACE));
        assertEquals(1, player.getHandSize(), "Expected handSize to be 1 after a card is added");
        
    }
    
    @Test
    public void testSetPlayerStateUpdatesPlayerState() {

        Player player = new Player("test", "Test Player");
        player.setPlayerState(PlayerState.FINISHED);
        assertEquals(PlayerState.FINISHED, player.getPlayerState(), "Expected Player to be in FINISHED state");
        
    }
    
    @Test
    public void testSetPlayerStateToNullThrowsException() {

        Player player = new Player("test", "Test Player");
        assertThrows(IllegalArgumentException.class, () -> player.setPlayerState(null), "Expected IllegalArgumentException to be thrown");
        
    }

    @Test
    public void testToStringWithNoFlags() {

        Player player = new Player("test", "Test Player");
        assertEquals("Player[ID: test; Name: Test Player; State: INIT; Hand: 0; Flags: none]", player.toString(), "toString result not as expected");
        
    }

    @Test
    public void testToStringWithFlags() {

        Player player = new Player("test", "Test Player");
        player.setPlayerDown();
        player.getPenaltyCard().setPenaltyCard(Card.as(Suit.CLUBS, Value.ACE));
        assertEquals("Player[ID: test; Name: Test Player; State: INIT; Hand: 0; Flags: PC,DOWN]", player.toString(), "toString result not as expected");
        
    }
    
}
