package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;

public class PickupDiscardActionTests
{

    private DiscardPile discardPile;
    private Player pickupPlayer, pickupPlayerDown;

    static final Card TEST_CARD = Card.as(Suit.CLUBS, Value.ACE);
    
    @BeforeEach
    public void mockDiscardPile() {

        discardPile = Mockito.mock(DiscardPile.class);
        Mockito.when(discardPile.pickup()).thenReturn(TEST_CARD);
        
    }

    @BeforeEach
    public void mockPlayers() {

        pickupPlayer = Mockito.mock(Player.class);
        Mockito.when(pickupPlayer.getHand()).thenReturn(new Hand());
        Mockito.when(pickupPlayer.getHandSize()).thenReturn(1);
        Mockito.when(pickupPlayer.getPlayerID()).thenReturn("pickup-player");
        Mockito.when(pickupPlayer.getPlayerState()).thenReturn(PlayerState.PICKUP);
        Mockito.when(pickupPlayer.isDown()).thenReturn(false);

        pickupPlayerDown = Mockito.mock(Player.class);
        Mockito.when(pickupPlayerDown.getHand()).thenReturn(new Hand());
        Mockito.when(pickupPlayer.getHandSize()).thenReturn(1);
        Mockito.when(pickupPlayerDown.getPlayerID()).thenReturn("pickup-player-down");
        Mockito.when(pickupPlayerDown.getPlayerState()).thenReturn(PlayerState.PICKUP);
        Mockito.when(pickupPlayerDown.isDown()).thenReturn(true);
        
    }

    @Test
    public void testPickupDiscardActionReturnsCard() {

        Card pickupCard = new PickupDiscardActionHandler(discardPile).newAction("test-request", 0, "pickup-player").pickupDiscard(pickupPlayer);
        assertEquals(TEST_CARD, pickupCard, "Expected pickupCard to equal card");

    }

    @Test
    public void testPickupDiscardActionAddsCardToPlayerHand() {

        new PickupDiscardActionHandler(discardPile).newAction("test-request", 0, "pickup-player").pickupDiscard(pickupPlayer);
        assertTrue(pickupPlayer.getHand().getCardsStream().anyMatch(TEST_CARD::equals), "Expected to find pickup card in player hand");

    }
}
