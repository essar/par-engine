package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.cards.PrialPlay;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.actions.PlayCardsActionHandler.PlayCardsAction;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.plays.PlaysService;

/**
 * Test cases for {@link PlayCardsAction} and {@link PlayCardsActionHandler}.
 * @author @essar
 */
public class PlayCardsActionTests
{

    private Player player;
    private PlaysService plays;

    static final Card[] TEST_CARDS = new Card[] {
        Card.as(Suit.CLUBS, Value.ACE),
        Card.as(Suit.DIAMONDS, Value.ACE),
        Card.as(Suit.HEARTS, Value.ACE)
    };
    static final PrialPlay PLAY = new PrialPlay();
        
    
    @BeforeEach
    public void mockPlays() {

        plays = Mockito.mock(PlaysService.class);
        Mockito.when(plays.buildPlayForPlayer(player, TEST_CARDS)).thenReturn(PLAY);
        
    }

    @BeforeEach
    public void mockPlayer() {

        player = Mockito.mock(Player.class);
        
        Hand player1Hand = new Hand();
        Arrays.stream(TEST_CARDS).forEach(player1Hand::addCard);
        
        Mockito.when(player.getHand()).thenReturn(player1Hand);
        Mockito.when(player.getHandSize()).thenReturn(TEST_CARDS.length);
        Mockito.when(player.getPlayerID()).thenReturn("playing-player");
        Mockito.when(player.getPlayerState()).thenReturn(PlayerState.PLAYING);
        Mockito.when(player.isDown()).thenReturn(false);
        
    }

    @Test
    public void testPlayCardsReturnsExpectedPlay() {

        Play play = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player").playCards(player, TEST_CARDS);
        assertEquals(PLAY, play, "Expected play cards to equal play");

    }

    @Test
    public void testPlayCardsReturnsPlayWithExpectedCards() {

        Play play = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player").playCards(player, TEST_CARDS);
        assertArrayEquals(PLAY.getCards(), play.getCards(), "Expected play cards to equal cards");

    }

    @Test
    public void testPlayCardsRemovesExpectedCardsFromlayerHand() {

        new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player").playCards(player, TEST_CARDS);
        assertFalse(player.getHand().getCardsStream().anyMatch(c -> Arrays.binarySearch(TEST_CARDS, c) >= 0), "Expected not to find play cards in player hand");

    }

    @Test
    public void testPlayCardsSetsResultToExpectedPlay() {

        PlayCardsAction action = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player");
        action.playCards(player, TEST_CARDS);
        assertEquals(PLAY, action.getResult(), "Expected result to be created play");

    }

    @Test
    public void testPlayCardsThrowsExceptionForNullPlayer() {

        PlayCardsAction action = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player");
        assertThrows(IllegalArgumentException.class, () -> action.playCards(null, TEST_CARDS), "Expected IllegalArgumentException");

    }

    @Test
    public void testPlayCardsThrowsExceptionForNullCards() {

        PlayCardsAction action = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player");
        assertThrows(IllegalArgumentException.class, () -> action.playCards(player, null), "Expected IllegalArgumentException");

    }

    @Test
    public void testRunActionSetsResultToExpectedCard() {

        PlayCardsAction action = new PlayCardsActionHandler(plays).newAction("test-request", 0, "playing-player");
        action.addActionParameter("cards", TEST_CARDS);
        action.runAction(player);
        assertEquals(PLAY, action.getResult(), "Expected result to be created play");

    }
}
