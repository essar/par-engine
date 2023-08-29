package uk.co.essarsoftware.par.engine.core.app.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.core.app.CardNotInHandException;
import uk.co.essarsoftware.par.engine.core.app.TestCard;
import uk.co.essarsoftware.par.engine.core.app.players.PlayersServiceImpl;
import uk.co.essarsoftware.par.engine.core.app.plays.PlaysServiceImpl;
import uk.co.essarsoftware.par.engine.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.players.Player;

@ExtendWith(SpringExtension.class)
public class ActionsServiceTests
{

    @MockBean
    private EngineEventQueue eventQueue;
    @MockBean
    private PlayersServiceImpl players;
    @MockBean
    private PlaysServiceImpl plays;
    @MockBean
    private ActionSequencer actionSequencer;
    @MockBean
    private DrawPile drawPile;
    @MockBean
    private DiscardPile discardPile;
    
    private ActionsService svc;

    private Player playerWithHand(Card... cards) {

        Player player = new Player("test", "Test Player");

        Arrays.stream(cards)
            .map(TestCard::new)
            .forEach(player.getHand()::addCard);
        
        return player;

    }

    @BeforeEach
    public void setUpService() {

        svc = new ActionsService(eventQueue, players, plays, actionSequencer, drawPile, discardPile);

    }

    @Test
    public void testResolveCardsSingleCard() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        
        // Set up mock
        when(players.getCurrentPlayer()).thenReturn(playerWithHand(card));

        Card resolvedCard = svc.resolveCards(players.getCurrentPlayer(), card)[0];
        assertEquals(card.getSuit(), resolvedCard.getSuit(), "Resolved card suit should match input");
        assertEquals(card.getValue(), resolvedCard.getValue(), "Resolved card value should match input");

    }
    
    @Test
    public void testResolveCardsMultipleCards() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.DIAMONDS, Value.JACK)
        };
        // Set up mock
        when(players.getCurrentPlayer()).thenReturn(playerWithHand(cards));

        Card[] resolvedCards = svc.resolveCards(players.getCurrentPlayer(), cards);
        assertEquals(cards.length, resolvedCards.length, "Length of cards and resolveCards should match");
        
    }

    @Test
    public void testResolveCardsWithDuplicateCardInHandIsOK() {

        Card[] handCards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.CLUBS, Value.ACE)
        };
        // Set up mock
        when(players.getCurrentPlayer()).thenReturn(playerWithHand(handCards));

        Card[] resolvedCards = svc.resolveCards(players.getCurrentPlayer(), handCards);
        assertEquals(handCards.length, resolvedCards.length, "Length of cards and resolveCards should match");

    }

    @Test
    public void testResolveCardsWithMissingCardRaisesException() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        // Set up mock
        when(players.getCurrentPlayer()).thenReturn(playerWithHand(card));

        Card searchCard = Card.as(Suit.CLUBS, Value.KING);

        assertThrows(CardNotInHandException.class, () -> svc.resolveCards(players.getCurrentPlayer(), searchCard), "Missing card should throw exception");

    }

    @Test
    public void testResolveCardsWithDuplicateCardRaisesException() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        // Set up mock
        when(players.getCurrentPlayer()).thenReturn(playerWithHand(card));

        Card[] searchCards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.CLUBS, Value.ACE)
        };

        assertThrows(CardNotInHandException.class, () -> svc.resolveCards(players.getCurrentPlayer(), searchCards), "Duplicate card should throw exception");

    }
    
}
