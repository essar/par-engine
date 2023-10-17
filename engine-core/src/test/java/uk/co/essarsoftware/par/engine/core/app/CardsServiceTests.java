package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Pack;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.core.exceptions.CardNotInHandException;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayersService;

@ExtendWith(SpringExtension.class)
public class CardsServiceTests
{

    @MockBean
    private PlayersService players;
    @MockBean
    private DiscardPile discardPile;
    @MockBean
    private DrawPile drawPile;

    private Hand hand;
    private Player player;

    private CardsService underTest;


    @BeforeEach
    public void initService() {

        underTest = new CardsService(players, drawPile, discardPile);

    }
    
    @BeforeEach
    public void initDrawPile() {

        when(drawPile.pickup()).thenAnswer(invocation -> Card.as(Suit.CLUBS, Value.ACE));

    }

    @BeforeEach
    public void initPlayers() {

        when(players.getPlayersStream()).thenAnswer(invocation -> Stream.of(player));

    }

    @BeforeEach
    public void mockHand() {

        hand = mock(Hand.class);

    }

    @BeforeEach
    public void mockPlayer() {

        player = mock(Player.class);
        when(player.getHand()).thenAnswer(invocation -> hand);

    }

    @Test
    public void testDealHandsClearsHands() {

        underTest.dealHands();
        verify(hand).clear();

    }

    @Test
    public void testDealHandsShufflesDeck() {

        underTest.dealHands();
        verify(drawPile).shuffle();

    }

    @Test
    public void testDealHandsDeals11CardsToPlayer() {

        underTest.dealHands();
        verify(hand, times(11)).addCard(any(Card.class));

    }

    @Test
    public void testDealHandsAddsCardToDiscardPile() {

        underTest.dealHands();
        verify(discardPile).discard(any(Card.class));

    }

    @Test
    public void testDealHandsMoves12CardsFromDrawPile() {

        underTest.dealHands();
        verify(drawPile, times(12)).pickup();

    }

    @Test
    public void testInitPilesClearsDiscardPile() {

        underTest.initPiles();
        verify(discardPile).clear();

    }

    @Test
    public void testInitPilesClearsDrawPile() {

        underTest.initPiles();
        verify(drawPile).clear();
        
    }

    @Test
    public void testInitPilesFillsDrawPileWithPack() {

        underTest.initPiles();
        verify(drawPile).addPack(any(Pack.class));
        
    }

    @Test
    public void testResolveCardsReturnsKnownCards() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        when(hand.getCardsStream()).thenAnswer(invocation -> Stream.of(card));
        assertArrayEquals(new Card[] { card }, underTest.resolveCards(player, card), "Expected to resolve a known card");

    }

    @Test
    public void testResolveCardsThrowsExceptionForUnknownCard() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        Card unknownCard = Card.as(Suit.CLUBS, Value.TWO);
        when(hand.getCardsStream()).thenAnswer(invocation -> Stream.of(card));
        assertThrows(CardNotInHandException.class, () -> underTest.resolveCards(player, unknownCard), "Expected CardNotInHandException for unknown card");

    }

    @Test
    public void testResolveCardsThrowsExceptionForDuplicateCard() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        when(hand.getCardsStream()).thenAnswer(invocation -> Stream.of(card));
        assertThrows(CardNotInHandException.class, () -> underTest.resolveCards(player, card, card), "Expected CardNotInHandException for duplicate card");

    }
}
