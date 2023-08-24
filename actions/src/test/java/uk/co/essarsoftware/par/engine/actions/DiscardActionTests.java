package uk.co.essarsoftware.par.engine.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class DiscardActionTests
{

    private DiscardPile discardPile;
    private Player player;

    static final Card TEST_CARD = Card.as(Suit.CLUBS, Value.ACE);
    
    @BeforeEach
    public void mockDiscardPile() {

        discardPile = Mockito.mock(DiscardPile.class);
        
    }

    @BeforeEach
    public void mockPlayers() {

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
    public void testDiscardActionReturnsExpectedCard() {

        Card discard = new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player").discard(player, TEST_CARD);
        assertEquals(TEST_CARD, discard, "Expected discard to equal card");

    }

    @Test
    public void testDiscardActionRemovesExpectedCardFromPlayerHand() {

        new DiscardActionHandler(discardPile).newAction("test-request", 0, "discard-player").discard(player, TEST_CARD);
        assertFalse(player.getHand().getCardsStream().anyMatch(TEST_CARD::equals), "Expected not to find discarded card in player hand");

    }
}
