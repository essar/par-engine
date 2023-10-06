package uk.co.essarsoftware.par.engine.core.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Test cases for {@link CreatePlayerResponse}.
 * @author @essar
 */
public class CreatePlayerResponseTests
{

    private Player player;

    @BeforeEach
    public void initPlayer() {

        player = new Player("test", "Test Player");
        
    }

    @Test
    public void testCreatePlayerResponseSetsPlayerID() {

        CreatePlayerResponse rsp = new CreatePlayerResponse(player);
        assertEquals("test", rsp.getPlayerID(), "Expected PlayerID to be set");

    }

    @Test
    public void testCreatePlayerResponseSetsPlayerName() {

        CreatePlayerResponse rsp = new CreatePlayerResponse(player);
        assertEquals("Test Player", rsp.getPlayerName(), "Expected PlayerName to be set");

    }

    @Test
    public void testCreatePlayerResponseSetsPlayerState() {

        CreatePlayerResponse rsp = new CreatePlayerResponse(player);
        assertEquals("INIT", rsp.getPlayerState(), "Expected PlayerState to be set");

    }

    @Test
    public void testToStringReturnsExpectedValue() {

        CreatePlayerResponse rsp = new CreatePlayerResponse(player);
        assertEquals("Created Player test", rsp.toString(), "Did not get expected response from toString");

    }
}
