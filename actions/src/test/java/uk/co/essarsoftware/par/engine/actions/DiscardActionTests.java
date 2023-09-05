package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.actions.DiscardActionHandler.DiscardAction;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

/**
 * Test cases for {@link DiscardAction} and {@link DiscardActionHandler}.
 * @author @essar
 */
public class DiscardActionTests
{

    private DiscardPile discardPile;
    private Player player;

    static final Card TEST_CARD = Card.as(Suit.CLUBS, Value.ACE);
    
    @BeforeEach
    public void initDiscardPile() {

        discardPile = new DiscardPile();
        
    }

    @BeforeEach
    public void mockPlayer() {

        player = Mockito.mock(Player.class);

        Hand player1Hand = new Hand();
        player1Hand.addCard(TEST_CARD);

        Mockito.when(player.getHand()).thenReturn(player1Hand);
        Mockito.when(player.getHandSize()).thenReturn(1);
        Mockito.when(player.getPlayerID()).thenReturn("discard-player");
        Mockito.when(player.getPlayerState()).thenReturn(PlayerState.PLAYING);
        Mockito.when(player.isDown()).thenReturn(false);
        
    }

    @Test
    public void testDiscardReturnsExpectedCard() {

        Card discard = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player").discard(player, TEST_CARD);
        assertEquals(TEST_CARD, discard, "Expected discard to equal card");

    }

    @Test
    public void testDiscardRemovesExpectedCardFromPlayerHand() {

        new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player").discard(player, TEST_CARD);
        assertFalse(player.getHand().getCardsStream().anyMatch(TEST_CARD::equals), "Expected not to find discarded card in player hand");

    }

    @Test
    public void testDiscardAddsExpectedCardToDiscardPile() {

        new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player").discard(player, TEST_CARD);
        assertEquals(TEST_CARD, discardPile.getDiscard(), "Expected to find card in discard pile");

    }

    @Test
    public void testDiscardSetsResultToExpectedCard() {

        DiscardAction action = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player");
        action.discard(player, TEST_CARD);
        assertEquals(TEST_CARD, action.getResult(), "Expected result to be discarded card");

    }

    @Test
    public void testDiscardThrowsExceptionForNullPlayer() {

        DiscardAction action = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player");
        assertThrows(IllegalArgumentException.class, () -> action.discard(null, TEST_CARD), "Expected IllegalArgumentException");

    }

    @Test
    public void testDiscardThrowsExceptionForNullCard() {

        DiscardAction action = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player");
        assertThrows(IllegalArgumentException.class, () -> action.discard(player, null), "Expected IllegalArgumentException");

    }

    @Test
    public void testRunActionSetsResultToExpectedCard() {

        DiscardAction action = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player");
        action.addActionParameter("card", TEST_CARD);
        action.runAction(player);
        assertEquals(TEST_CARD, action.getResult(), "Expected result to be discarded card");

    }

    @Test
    public void testRunActionThrowsExceptionIfCardNotSet() {

        DiscardAction action = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player");
        assertThrows(IllegalArgumentException.class, () -> action.runAction(player), "Expected IllegalArgumentException");

    }
}
