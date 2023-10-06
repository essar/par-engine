package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.core.exceptions.GameStateException;
import uk.co.essarsoftware.par.engine.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.players.PlayersService;
import uk.co.essarsoftware.par.engine.plays.PlaysService;

/**
 * Test cases for {@link GameServiceImpl}.
 * @author @essar
 */
@ExtendWith(SpringExtension.class)
public class GameServiceImplTests
{

    @MockBean
    private CardsService cardsSvc;
    @MockBean
    private EngineEventQueue eventQueue;
    @MockBean
    private Game game;
    @MockBean
    private PlayersService playersSvc;
    @MockBean
    private PlaysService playsSvc;

    private Player player;

    private GameServiceImpl underTest;

    @BeforeEach
    public void initGameSvc() {

        underTest = new GameServiceImpl(game, eventQueue, playersSvc, playsSvc, cardsSvc);

    }

    @BeforeEach
    public void initPlayersSvc() {

        when(playersSvc.getPlayersStream()).thenAnswer((invocation) -> Stream.of(player));

    }

    @BeforeEach
    public void mockPlayer() {

        player = mock(Player.class);

    }


    @Test
    public void testGetGameReturnsGame() {

        assertEquals(game, underTest.getGame());
        
    }

    @Test
    public void testStartNextReturnsSetsNextRound() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        assertEquals(Round.PP, underTest.startNextRound());

    }

    @Test
    public void testStartNextRoundEndsGameWhenInLastRound() {

        when(game.getCurrentRound()).thenReturn(Round.RRR);

        assertEquals(Round.END, underTest.startNextRound());

    }


    @Test
    public void testStartNextRoundSetsNextRound() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(game).setCurrentRound(Round.PP);

    }

    @Test
    public void testStartNextRoundInitializesPlayerRound() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(playersSvc).startNewRound();

    }

    @Test
    public void testStartNextRoundDealsCards() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(cardsSvc).dealHands();

    }

    @Test
    public void testStartNextRoundInitializesPiles() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(cardsSvc).initPiles();

    }

    @Test
    public void testStartNextRoundInitializesPlays() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(playsSvc).initPlaysForRound(Round.PP);

    }

    @Test
    public void testStartNextRoundResetsTurnCount() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(game).resetTurnCount();

    }

    @Test
    public void testStartNextRoundSetsPlayersToWatching() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        verify(playersSvc).setPlayerState(player, PlayerState.WATCHING);

    }

    @Test
    public void testStartNextRoundQueuesRoundStartedEvent() {

        when(game.getCurrentRound()).thenReturn(Round.START);

        underTest.startNextRound();
        ArgumentCaptor<RoundStartedEvent> eventArg = ArgumentCaptor.forClass(RoundStartedEvent.class);
        verify(eventQueue).queueEvent(eventArg.capture());
        assertEquals(Round.PP, eventArg.getValue().getRound(), "RoundStartedEvent queued with PP Round");

    }

    @Test
    public void testStartNextRoundWhenInEndStateThrowsException() {

        when(game.getCurrentRound()).thenReturn(Round.END);

        assertThrows(GameStateException.class, () -> underTest.startNextRound(), "GameStateException should be thrown when Game in END state");
        
    }
    
}
