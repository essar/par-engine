package uk.co.essarsoftware.par.engine.core.app.players;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
public class PlayersTextController
{
    private static final Logger _LOG = LoggerFactory.getLogger(PlayersTextController.class);

    private final PlayersService playersSvc;


    public PlayersTextController(final PlayersService playersSvc) {

        this.playersSvc = playersSvc;

    }

    private static Mono<String> handleException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());
        return Mono.error(e);

    }

    @GetMapping(path = "/players")
    public Mono<String> getPlayers() {

        return Mono.just(playersSvc.getPlayersStream())
            .map(GetPlayersResponse::new)
            .map(GetPlayersResponse::toString);

    }

    @PostMapping(path = "/players")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<String> createPlayer(@RequestParam(name = "player_name", required = true) final String playerName) {

        return Mono.fromSupplier(() -> playersSvc.createPlayer(playerName))
            .map(CreatePlayerResponse::new)
            .map(CreatePlayerResponse::toString)
            .onErrorResume(PlayersTextController::handleException);

    }

    @GetMapping(path = "/players/{player_id}")
    public Mono<String> getPlayer(@PathVariable(name = "player_id") final String playerID) {

        return Mono.fromSupplier(() -> playersSvc.getPlayer(playerID))
            .map(GetPlayerResponse::new)
            .map(GetPlayerResponse::toString)
            .onErrorResume(PlayersTextController::handleException);

    }

    @GetMapping(path = "/players/{player_id}/hand")
    public Mono<String> getPlayerHand(@PathVariable(name = "player_id") final String playerID) {

        return Mono.fromSupplier(() -> playersSvc.getPlayer(playerID))
            .map(GetPlayerHandResponse::new)
            .map(GetPlayerHandResponse::toString)
            .onErrorResume(PlayersTextController::handleException);

    }
}
