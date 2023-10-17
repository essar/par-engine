package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.core.responses.GetDiscardResponse;
import uk.co.essarsoftware.par.engine.core.responses.GetGameResponse;
import uk.co.essarsoftware.par.engine.core.responses.StartRoundResponse;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.GameService;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerList;
import uk.co.essarsoftware.par.engine.players.PlayerState;

@ExtendWith(SpringExtension.class)
public class GameControllerTests
{

    @MockBean
    private DiscardPile discardPile;
    @MockBean
    private Game game;
    @MockBean
    private GameService gameSvc;
    @MockBean
    private PlayerList players;
    
    private GameController underTest;

    @BeforeEach
    public void initGameController() {

        underTest = new GameController(game, players, gameSvc, discardPile);
        
    }

    @Test
    public void testGetGameReturnsCurrentRound() {

        when(game.getCurrentRound()).thenReturn(Round.PP);

        GetGameResponse response = underTest.getGame().block();
        assertEquals(Round.PP, response.getCurrentRound(), "Response should contain current round from Game");

        assertNotNull(response, "Non-null response returned");
        
    }

    @Test
    public void testGetGameReturnsDealer() {

        Player player = mock(Player.class);
        when(player.getPlayerID()).thenReturn("test_dealer");
        when(players.getDealer()).thenReturn(player);

        GetGameResponse response = underTest.getGame().block();
        assertEquals("test_dealer", response.getDealer(), "Response should contain dealer from Player List");
        
    }

    @Test
    public void testGetGameReturnsCurrentPlayerWithPlayerID() {

        Player player = mock(Player.class);
        when(player.getPlayerID()).thenReturn("test_player");
        when(players.getCurrentPlayer()).thenReturn(player);

        GetGameResponse response = underTest.getGame().block();
        assertEquals("test_player", response.getCurrentPlayer().getPlayerID(), "Response should contain player with ID from Game");

    }

    @Test
    public void testGetGameReturnsCurrentPlayerWithPlayerName() {

        Player player = mock(Player.class);
        when(player.getPlayerName()).thenReturn("Test Player");
        when(players.getCurrentPlayer()).thenReturn(player);

        GetGameResponse response = underTest.getGame().block();
        assertEquals("Test Player", response.getCurrentPlayer().getPlayerName(), "Response should contain player with name from Game");

    }

    @Test
    public void testGetGameReturnsCurrentPlayerWithPlayerState() {

        Player player = mock(Player.class);
        when(player.getPlayerState()).thenReturn(PlayerState.WATCHING);
        when(players.getCurrentPlayer()).thenReturn(player);

        GetGameResponse response = underTest.getGame().block();
        assertEquals("WATCHING", response.getCurrentPlayer().getPlayerState(), "Response should contain player with state from Game");

    }

    @Test
    public void testGetGameReturnsGameID() {

        when(game.getGameID()).thenReturn("test_game");

        GetGameResponse response = underTest.getGame().block();
        assertEquals("test_game", response.getGameID(), "Response should contain game ID from Game");

    }

    @Test
    public void testGetGameReturnsPlayerCount() {

        when(players.size()).thenReturn(1);

        GetGameResponse response = underTest.getGame().block();
        assertEquals(1, response.getPlayerCount(), "Response should contain player count from Player List");

    }

    @Test
    public void testGetGameReturnsTurnCount() {

        when(game.getTurnCount()).thenReturn(1);

        GetGameResponse response = underTest.getGame().block();
        assertEquals(1, response.getTurnCount(), "Response should contain turn count from Game");

    }

    @Test
    public void testGetDiscardJsonReturnsCard() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        when(discardPile.getDiscard()).thenReturn(card);

        GetDiscardResponse response = underTest.getDiscardJson().block();
        assertEquals(card, response.getDiscard(), "Response should contain card from Table");

    }
    
    @Test
    public void testGetDiscardJsonThrowsExceptionWhenRaisedInService() {

        when(discardPile.getDiscard()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> underTest.getDiscardJson().block(), "Exception from service should be propagated");
        
    }

    @Test
    public void testGetDiscardTextsReturnsCardValue() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        when(discardPile.getDiscard()).thenReturn(card);

        String response = underTest.getDiscardText().block();
        assertEquals("1C", response, "Response should be card string");

    }
    
    @Test
    public void testGetDiscardTextThrowsExceptionWhenRaisedInService() {

        when(discardPile.getDiscard()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> underTest.getDiscardText().block(), "Exception from service should be propagated");
        
    }

    @Test
    public void testStartRoundCallsService() {

        underTest.startRoundJson().block();
        verify(gameSvc).startNextRound();
        
    }

    @Test
    public void testStartRoundReturnsRoundFromService() {

        Player player = mock(Player.class);
        when(gameSvc.startNextRound()).thenReturn(Round.PP);
        when(players.getCurrentPlayer()).thenReturn(player);

        StartRoundResponse response = underTest.startRoundJson().block();
        assertEquals(Round.PP, response.getRound(), "Response should contain round from Service");
        
    }
    
    @Test
    public void testStartRoundReturnsCurrentPlayerFromPlayers() {

        Player player = mock(Player.class);
        when(player.getPlayerID()).thenReturn("test");
        when(gameSvc.startNextRound()).thenReturn(Round.PP);
        when(players.getCurrentPlayer()).thenAnswer((invocation) -> player);

        StartRoundResponse response = underTest.startRoundJson().block();
        assertEquals("test", response.getCurrentPlayerID(), "Response should contain PlayerID of current player in player list");
        
    }
    
    @Test
    public void testStartRoundThrowsExceptionWhenRaisedInService() {

        when(gameSvc.startNextRound()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> underTest.startRoundJson().block(), "Exception from service should be propagated");
        
    }
}
