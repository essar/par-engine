package uk.co.essarsoftware.par.engine.core.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.engine.core.responses.GetDiscardResponse;
import uk.co.essarsoftware.par.engine.core.responses.GetGameResponse;
import uk.co.essarsoftware.par.engine.core.responses.StartRoundResponse;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.game.GameService;
import uk.co.essarsoftware.par.engine.players.PlayerList;

/**
 * Controller class for game/table functions.
 * @author @essar
 */
@RestController
public class GameController
{
    private static final Logger _LOG = LoggerFactory.getLogger(GameController.class);

    private final DiscardPile discardPile;
    private final Game game;
    private final PlayerList players;
    private final GameService gameSvc;


    /**
     * Instantiate new Game Controller.
     * @param game current Game object.
     * @param players list holding players within the game.
     * @param gameSvc service exposing game functions.
     * @param discardPile current game discard pile.
     */
    public GameController(Game game, PlayerList players, GameService gameSvc, DiscardPile discardPile) {

        this.game = game;
        this.players = players;
        this.gameSvc = gameSvc;
        this.discardPile = discardPile;

    }

    /**
     * Additional functionality performed during error handling.
     * @param e the exception thrown.
     */
    private static void defaultErrorProcessor(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());

    }

    /**
     * Get the current game object.
     * @return the current game, encapsulated using {@link GetGameResponse}.
     */
    @GetMapping(path = "/game", produces = "application/json")
    public Mono<GetGameResponse> getGame() {

        return Mono.fromCallable(() -> new GetGameResponse(game, players));

    }
    
    /**
     * Start a new round in the game.
     * @return the new round state, encapsulated using {@link StartRoundResponse}.
     */
    @PostMapping(path = "/game/startRound", produces = "application/json")
    public Mono<StartRoundResponse> startRoundJson() {

        return Mono.fromCallable(gameSvc::startNextRound)
            .map(round -> new StartRoundResponse(round, players.getCurrentPlayer().getPlayerID()))
            .doOnError(GameController::defaultErrorProcessor);

    }

    /**
     * Get the card at the top of the discard pile.
     * @return the discard, encapsulated using {@link GetDiscardResponse}.
     */
    @GetMapping(path = "/table/discard", produces = "application/json")
    public Mono<GetDiscardResponse> getDiscardJson() {

        return Mono.fromCallable(discardPile::getDiscard)
            .map(GetDiscardResponse::new)
            .doOnError(GameController::defaultErrorProcessor);
    
    }

     /**
     * Get the card at the top of the discard pile.
     * @return the discard, formatted as a simple String.
     */
    @GetMapping(path = "/table/discard", produces = "text/plain")
    public Mono<String> getDiscardText() {

        return Mono.fromCallable(discardPile::getDiscard)
            .map(CardEncoder::asShortString)
            .doOnError(GameController::defaultErrorProcessor);

    }
}
