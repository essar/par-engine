package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.actions.PickupDrawActionHandler.PickupDrawAction;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

/**
 * Test cases for {@link PickupDrawAction} and {@link PickupDrawActionHandler}.
 * @author @essar
 */
public class PickupDrawActionTests
{

    private DrawPile drawPile;
    private Player player;

    static final Card TEST_CARD = Card.as(Suit.CLUBS, Value.ACE);
    
    @BeforeEach
    public void mockDrawPile() {

        drawPile = Mockito.mock(DrawPile.class);
        Mockito.when(drawPile.pickup()).thenReturn(TEST_CARD);
        
    }

    @BeforeEach
    public void mockPlayers() {

        player = Mockito.mock(Player.class);
        Mockito.when(player.getHand()).thenReturn(new Hand());
        Mockito.when(player.getHandSize()).thenReturn(1);
        Mockito.when(player.getPlayerID()).thenReturn("pickup-player");
        Mockito.when(player.getPlayerState()).thenReturn(PlayerState.PICKUP);
        Mockito.when(player.isDown()).thenReturn(false);
        
    }

    @Test
    public void testPickupDrawActionReturnsExpectedCard() {

        Card pickupCard = new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player").pickupDraw(player);
        assertEquals(TEST_CARD, pickupCard, "Expected pickupCard to equal card");

    }

    @Test
    public void testPickupDrawActionAddsExpectedCardToPlayerHand() {

        new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player").pickupDraw(player);
        assertTrue(player.getHand().getCardsStream().anyMatch(TEST_CARD::equals), "Expected to find pickup card in player hand");

    }

    @Test
    public void testPickupDrawCallsDrawPilePickup() {

        new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player").pickupDraw(player);
        verify(drawPile).pickup();

    }

    @Test
    public void testPickupDrawSetsResultToExpectedCard() {

        PickupDrawAction action = new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player");
        action.pickupDraw(player);
        assertEquals(TEST_CARD, action.getResult(), "Expected result to be picked up card");

    }

    @Test
    public void testPickupDrawThrowsExceptionForNullPlayer() {

        PickupDrawAction action = new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player");
        assertThrows(IllegalArgumentException.class, () -> action.pickupDraw(null), "Expected IllegalArgumentException");

    }

    @Test
    public void testRunActionSetsResultToExpectedCard() {

        PickupDrawAction action = new PickupDrawActionHandler(drawPile).newAction("test-request", 0, "pickup-player");
        action.runAction(player);
        assertEquals(TEST_CARD, action.getResult(), "Expected result to be picked up card");

    }
}
