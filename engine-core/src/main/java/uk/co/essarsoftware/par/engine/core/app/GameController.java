package uk.co.essarsoftware.par.engine.core.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.players.PlayersJsonController;
import uk.co.essarsoftware.par.engine.events.EngineEvent;
import uk.co.essarsoftware.par.engine.events.EventProcessor;
import uk.co.essarsoftware.par.engine.game.Game;
import uk.co.essarsoftware.par.engine.players.PlayerList;

@RestController
public class GameController
{
    private static final Logger _LOG = LoggerFactory.getLogger(PlayersJsonController.class);

    @Autowired
    private CardsService cards;

    @Autowired
    private EventProcessor eventProcessor;

    @Autowired Game game;

    @Autowired PlayerList players;

    @Autowired
    private GameServiceImpl gameSvc;


    private static void logException(Throwable e) {

        _LOG.warn("[\u001B[31m{}\u001B[0m] {}", e.getClass().getSimpleName(), e.getMessage());

    }

    @GetMapping(path = "/game", produces = "application/json")
    public Mono<GetGameResponse> getGame() {

        return Mono.just(new GetGameResponse(game, players))
            .onErrorResume(e -> {
                logException(e);
                return Mono.error(e);
            });
    }
    
    // @GetMapping(path = "/game/events", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @GetMapping(path = "/game/events", produces = {"text/event-stream", "application/stream+json", "application/x-ndjson"})
    public Flux<String> streamEvents() throws InterruptedException {

        return Flux.fromStream(eventProcessor.getEventStream()).log().map(EngineEvent::toString);
            //.merge(Flux.interval(Duration.ofSeconds(10)).log().map(l -> Long.toString(l)));

    }

    @PostMapping(path = "/game/startRound", produces = "application/json")
    public Mono<StartRoundResponse> startRoundJson() {

        return Mono.fromCallable(() -> gameSvc.startNextRound());

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
