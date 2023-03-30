package uk.co.essarsoftware.par.engine.core.app;

import java.util.Objects;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.DiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDiscardActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionRequest.PickupDrawActionRequest;
import uk.co.essarsoftware.par.engine.core.app.ActionResponse.DiscardActionResponse;
import uk.co.essarsoftware.par.engine.core.app.ActionResponse.PickupDiscardActionResponse;
import uk.co.essarsoftware.par.engine.core.app.ActionResponse.PickupDrawActionResponse;
import uk.co.essarsoftware.par.engine.core.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.core.events.NextPlayerEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerDiscardEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerPickupDiscardEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerPickupDrawEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerStateChangeEvent;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.game.Player;
import uk.co.essarsoftware.par.game.PlayerState;

@Service
public class ActionsService
{

    private static final Logger _LOG = LoggerFactory.getLogger(ActionsService.class);

    @Autowired
    private DiscardPile discardPile;

    @Autowired
    private DrawPile drawPile;

    @Autowired
    private EngineEventQueue eventQueue;

    @Autowired
    private PlayersService players;

    private int sequenceNo = 1;


    private static boolean validateNotNull(Object obj, String message) {

        if (Objects.nonNull(obj)) {

            return true;

        }
        throw new InvalidActionRequestException(message);

    }

    static boolean validateRequest(ActionRequest request) {

        return validateNotNull(request.getActionType(), "Missing action_type")
            && validateNotNull(request.getActionSequence(), "Missing action_sequence");

    }

    synchronized <E extends ActionRequest, F extends ActionResponse> ActionResponse runAction(Function<E, F> actionFunction, E request) {

        System.out.println("running Action");

        _LOG.info("[\u001B[36m{}\u001B[0m] {}", request.getClass().getSimpleName(), request);

        if (request.getActionSequence() != this.sequenceNo) {

            throw new ActionOutOfSequenceException(request.getActionSequence(), this.sequenceNo);

        }
        F response = actionFunction.apply(request);

        // Increment sequence if action is successful
        this.sequenceNo ++;

        // Inject next sequence number into response
        response.setNextActionSequence(sequenceNo);

        return response;

    }

    public int getSequence() {

        return sequenceNo;

    }

    @PostConstruct
    public void registerProcessors() {

        // Activate player processor
        eventQueue.registerProcessor(NextPlayerEvent.class, event -> { activate(event.getPlayer()); });

        // Game started processor
        eventQueue.registerProcessor(RoundStartedEvent.class, event -> { activate(event.getCurrentPlayer()); });

        // Player end turn processor
        eventQueue.registerProcessor(PlayerStateChangeEvent.class, event -> event.getNewState() == PlayerState.DISCARDED, event -> { endTurn(event.getPlayer()); });
        
    }

    public void activate(final Player player) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(player.getPlayerID(), PlayerState.WATCHING);

        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.PICKUP);

    }

    public void endTurn(final Player player) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(player.getPlayerID(), PlayerState.DISCARDED);

        // Check if the player has remaining cards
        if (player.getHandSize() == 0) {

            // End the round

        }

        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.WATCHING);

        // Move to next player
        Player nextPlayer = players.nextPlayer();
        _LOG.info("{} is now the active player", nextPlayer.getPlayerName());

        eventQueue.queueEvent(new NextPlayerEvent(nextPlayer));
        
    }



    public DiscardActionResponse discard(final DiscardActionRequest request) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(request.getPlayerID(), PlayerState.PLAYING);

        // Resolve card from player hand
        Card handCard = currentPlayer.getHand().findCard(request.getCard());
        if (handCard == null) {

            // Card hasn't been found in hand
            throw new CardNotInHandException(String.format("Card %s not found in player hand", CardEncoder.asShortString(request.getCard())));

        }
        currentPlayer.getHand().removeCard(handCard);

        // Add card to discard pile
        discardPile.discard(handCard);
        
        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.DISCARDED);

        // Create response
        DiscardActionResponse response = new DiscardActionResponse(request);
        response.setCard(handCard);

        eventQueue.queueEvent(new PlayerDiscardEvent(currentPlayer, handCard));

        return response;

    }

    public PickupDiscardActionResponse pickupDiscard(final PickupDiscardActionRequest request) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(request.getPlayerID(), PlayerState.PICKUP);

        // Take card from discard pile
        Card card = discardPile.pickup();
        
        // Add card to player hand
        currentPlayer.getHand().addCard(card);
        
        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);

        // Create response
        PickupDiscardActionResponse response = new PickupDiscardActionResponse(request);
        response.setCard(card);

        eventQueue.queueEvent(new PlayerPickupDiscardEvent(currentPlayer, card));

        return response;

    }
    
    public PickupDrawActionResponse pickupDraw(final PickupDrawActionRequest request) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(request.getPlayerID(), PlayerState.PICKUP);

        // Take card from draw pile
        Card card = drawPile.pickup();
        
        // Add card to player hand
        currentPlayer.getHand().addCard(card);
        
        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);

        // Create response
        PickupDrawActionResponse response = new PickupDrawActionResponse(request);
        response.setCard(card);

        eventQueue.queueEvent(new PlayerPickupDrawEvent(currentPlayer));

        return response;

    }
}
