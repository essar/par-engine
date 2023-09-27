package uk.co.essarsoftware.par.engine.core.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import uk.co.essarsoftware.par.engine.core.app.CardsService;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.players.PlayersService;

/**
 * Test cases for {@link StartRoundTask}.
 */
public class StartRoundTaskTests
{

    private CardsService cards;
    private Player player1, player2;
    private PlayersService players;

    @BeforeEach
    public void initPlayers() {

        player1 = new Player("test1", "Test Player 1");
        player2 = new Player("test2", "Test Player 2");

    }

    @BeforeEach
    public void mockCardsService() {

        cards = Mockito.mock(CardsService.class);

    }

    @BeforeEach
    public void mockPlayersService() {

        players = Mockito.mock(PlayersService.class);
        when(players.getCurrentPlayer()).thenAnswer((invocation) -> player1);
        when(players.getPlayersStream()).thenAnswer((invocation) -> Stream.of(player1, player2));
        
    }

    @Test
    public void testProcessEventInitializesPiles() {

        StartRoundTask task = new StartRoundTask(cards, players);
        task.processEvent(new RoundStartedEvent(Round.START, player1));
        verify(cards).initPiles();

    }

    @Test
    public void testProcessEventDealsHands() {

        StartRoundTask task = new StartRoundTask(cards, players);
        task.processEvent(new RoundStartedEvent(Round.START, player1));
        verify(cards).dealHands();

    }

    @Test
    public void testProcessEventSetsCurrentPlayerState() {

        StartRoundTask task = new StartRoundTask(cards, players);
        task.processEvent(new RoundStartedEvent(Round.START, player1));
        assertEquals(PlayerState.PICKUP, player1.getPlayerState(), "Expected player to be in PICKUP state");

    }

    @Test
    public void testProcessEventSetsWatchingPlayerState() {

        StartRoundTask task = new StartRoundTask(cards, players);
        task.processEvent(new RoundStartedEvent(Round.START, player1));
        assertEquals(PlayerState.WATCHING, player2.getPlayerState(), "Expected player to be in WATCHING state");

    }

    
}
