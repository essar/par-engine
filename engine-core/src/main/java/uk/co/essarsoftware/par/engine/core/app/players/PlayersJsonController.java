package uk.co.essarsoftware.par.engine.core.app.players;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.engine.core.app.PlayersService;
import uk.co.essarsoftware.par.game.Game;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayersJsonController
{
    private static final Logger _LOG = LoggerFactory.getLogger(PlayersJsonController.class);

    @Autowired Game game;

    @Autowired
    private PlayersService playersSvc;


    private static void logException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());

    }

    @GetMapping(path = "/players")
    public Mono<GetPlayersResponse> getPlayers() {

        return Mono.fromSupplier(playersSvc::getPlayersStream)
            .map(GetPlayersResponse::new);

    }

    @PostMapping(path = "/players", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<CreatePlayerResponse> createPlayer(@RequestBody CreatePlayerRequest request) {

        return Mono.fromSupplier(() -> playersSvc.createPlayer(request.getPlayerName()))
            .map(CreatePlayerResponse::new)
            .onErrorResume(e -> {
                logException(e);
                return Mono.error(e);
            });

    }

    @GetMapping(path = "/players/{player_id}")
    public Mono<GetPlayerResponse> getPlayer(@PathVariable(name = "player_id") final String playerID) {

        return Mono.fromSupplier(() -> playersSvc.getPlayer(playerID))
            .map(GetPlayerResponse::new);

    }

    @DeleteMapping(path = "/players/{player_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<Void> deletePlayer(@PathVariable(name = "player_id") final String playerID) {

        return Mono.fromSupplier(() -> playersSvc.getPlayer(playerID))
            .doOnNext(playersSvc::removePlayer)
            .then();

    }

    @GetMapping(path = "/players/{player_id}/hand")
    public Mono<GetPlayerHandResponse> getPlayerHand(@PathVariable(name = "player_id") final String playerID) {

        return Mono.fromSupplier(() -> playersSvc.getPlayer(playerID))
            .map(GetPlayerHandResponse::new);

    }
}
