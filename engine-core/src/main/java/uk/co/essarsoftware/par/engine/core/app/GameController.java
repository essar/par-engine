package uk.co.essarsoftware.par.engine.core.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.DiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDrawActionRequest;
import uk.co.essarsoftware.par.engine.core.events.EngineEvent;
import uk.co.essarsoftware.par.engine.core.events.EngineEventQueue;
import uk.co.essarsoftware.par.game.Player;

@RestController
public class GameController
{
    private static final Logger _LOG = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private ActionsService actions;

    @Autowired
    private CardsService cards;

    @Autowired
    private EngineEventQueue eventQueue;

    @Autowired
    private GameService game;

    @Autowired
    private PlayersService players;



    private static Mono<? extends ActionResponse> handleEngineException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());
        return Mono.error(e);

    }
    
    // @GetMapping(path = "/game/events", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @GetMapping(path = "/game/events", produces = {"text/event-stream", "application/stream+json", "application/x-ndjson"})
    public Flux<String> streamEvents() throws InterruptedException {

        return eventQueue.getEvents().log().map(EngineEvent::toString);
            //.merge(Flux.interval(Duration.ofSeconds(10)).log().map(l -> Long.toString(l)));

    }

    @PostMapping(path = "/game/startRound", produces = "application/json")
    public Mono<StartRoundResponse> startRoundJson() {

        return Mono.fromCallable(() -> game.startNextRound());

    }

    @GetMapping(path = "/players", produces = "application/json")
    public Mono<GetPlayersResponse> getPlayersJson() {

        return Mono.just(new GetPlayersResponse(players.getPlayersStream()));

    }

    @GetMapping(path = "/players", produces = "text/plain")
    public Mono<String> getPlayersText() {

        return getPlayersJson().map(GetPlayersResponse::toString);

    }

    @PostMapping(path = "/players", consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<CreatePlayerResponse> createPlayerJson(@RequestBody CreatePlayerRequest request) {

        CreatePlayerResponse response = new CreatePlayerResponse(players.createPlayer(request.getPlayerName()));
        return Mono.just(response);

    }

    @PostMapping(path = "/players", produces = "text/plain")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<String> createPlayerText(@RequestParam(name = "player_name", required = true) final String playerName) {

        CreatePlayerResponse response = new CreatePlayerResponse(players.createPlayer(playerName));
        return Mono.just(response.toString());

    }

    @GetMapping(path = "/players/{player_id}", produces = "application/json")
    public Mono<GetPlayerResponse> getPlayerJson(@PathVariable(name = "player_id") final String playerID) {

        GetPlayerResponse response = new GetPlayerResponse(players.getPlayer(playerID));
        return Mono.just(response);

    }

    @GetMapping(path = "/players/{player_id}", produces = "text/plain")
    public Mono<String> getPlayerText(@PathVariable(name = "player_id") final String playerID) {

        GetPlayerResponse response = new GetPlayerResponse(players.getPlayer(playerID));
        return Mono.just(response.toString());

    }

    @DeleteMapping(path = "/players/{player_id}", produces = "application/json")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<?> deletePlayerJson(@PathVariable(name = "player_id") final String playerID) {

        players.removePlayer(players.getPlayer(playerID));
        return Mono.empty();

    }

    @GetMapping(path = "/players/{player_id}/hand", produces = "application/json")
    public Mono<GetPlayerHandResponse> getPlayerHandJson(@PathVariable(name = "player_id") final String playerID) {

        Player player = players.getPlayer(playerID);
        GetPlayerHandResponse response = new GetPlayerHandResponse(player);
        return Mono.just(response);

    }

    @GetMapping(path = "/players/{player_id}/hand", produces = "text/plain")
    public Mono<String> getPlayerHandText(@PathVariable(name = "player_id") final String playerID) {

        Player player = players.getPlayer(playerID);
        GetPlayerHandResponse response = new GetPlayerHandResponse(player);
        return Mono.just(response.toString());

    }

    @PostMapping(path = "/actions/discard", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> discardJson(@RequestBody final DiscardActionRequest request) {

        return Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::discard, rq))
            .onErrorResume(GameController::handleEngineException);
    }

    @PostMapping(path = "/actions/pickupDiscard", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> pickupDiscardJson(@RequestBody final PickupDiscardActionRequest request) {

        return Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::pickupDiscard, rq))
            .onErrorResume(GameController::handleEngineException);

    }

    @PostMapping(path = "/actions/pickupDraw", consumes = "application/json", produces = "application/json")
    public Mono<ActionResponse> pickupDrawJson(@RequestBody final PickupDrawActionRequest request) {

        System.out.println("in pickupDrawJson");
        Mono<ActionResponse> m = Mono.just(request)
            .filter(ActionsService::validateRequest)
            .map(rq -> actions.runAction(actions::pickupDraw, rq))
            .onErrorResume(GameController::handleEngineException);

        System.out.println("created Mono");

        return m;

    }

    @GetMapping(path = "/table/discard", produces = "application/json")
    public Mono<Map<String, Card>> getDiscardJson() {

        Card card = cards.getDiscard();
        return Mono.just(Map.of("card", card));

    }

    @GetMapping(path = "/table/discard", produces = "text/plain")
    public Mono<String> getDiscardText() {

        Card card = cards.getDiscard();
        return Mono.just(card.toString());

    }
    
}
