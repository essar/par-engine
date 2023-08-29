package uk.co.essarsoftware.par.engine.plays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.players.Player;

/**
 * Unit tests for {@link PlaySet}.
 */
public class PlaySetTests
{

    private Play availablePlay, validPlay;
    private Player player;
    

    @BeforeEach
    public void initPlayer() {

        player = new Player("test", "Test Player");

    }

    @BeforeEach
    public void mockPlays() {

        availablePlay = Mockito.mock(Play.class);
        when(availablePlay.getCards()).thenReturn(new Card[0]);

        validPlay = Mockito.mock(Play.class);
        when(validPlay.size()).thenReturn(3);

    }

    @Test
    public void testGetAvailablePlaysPlayerForKnownPlayerReturnsExpectedAvailablePlays() {

        PlaySet plays = new PlaySet();
        plays.put(player, new Play[] { availablePlay });
        assertArrayEquals(new Play[] { availablePlay }, plays.getAvailablePlays(player), "Expected play lists to match");

    }

    @Test
    public void testGetAvailablePlaysPlayerForKnownPlayerDoesNotReturnValidPlays() {

        PlaySet plays = new PlaySet();
        plays.put(player, new Play[] { validPlay });
        assertArrayEquals(new Play[0], plays.getAvailablePlays(player), "Expected empty array");

    }

    @Test
    public void testGetAvailablePlaysPlayerForUnknownPlayerReturnsEmptyArray() {

        PlaySet plays = new PlaySet();
        assertArrayEquals(new Play[0], plays.getAvailablePlays(player), "Expected empty array");

    }

    @Test
    public void testGetPlayerPlaysForKnownPlayerReturnsExpectedPlays() {

        PlaySet plays = new PlaySet();
        plays.put(player, new Play[] { availablePlay });
        assertArrayEquals(new Play[] { availablePlay }, plays.getPlayerPlays(player), "Expected play lists to match");

    }

    @Test
    public void testGetPlayerPlaysForUnknownPlayerReturnsNull() {

        PlaySet plays = new PlaySet();
        assertNull(plays.getPlayerPlays(player), "Expected to return empty array");

    }
    
}
