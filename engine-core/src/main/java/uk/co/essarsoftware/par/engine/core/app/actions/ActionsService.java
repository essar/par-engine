package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.core.app.CardNotInHandException;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.DiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDiscardAction;
import uk.co.essarsoftware.par.engine.core.app.actions.Action.PickupDrawAction;
import uk.co.essarsoftware.par.engine.core.app.players.PlayersService;
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

    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private final EngineEventQueue eventQueue;
    private final PlayersService players;

    private int sequenceNo = 1;

    @Autowired
    public ActionsService(final EngineEventQueue eventQueue, final PlayersService players, final DrawPile drawPile, final DiscardPile discardPile) {

        this.eventQueue = eventQueue;
        this.players = players;
        this.drawPile = drawPile;
        this.discardPile = discardPile;

    }

    private boolean validateSequence(Action action) {

        _LOG.debug("Validating sequence number");
        if (action.getActionSequence() == sequenceNo) {

            return true;    

        }
        throw new ActionOutOfSequenceException(action.getActionSequence(), sequenceNo);
    
    }


    synchronized <A extends Action<E>, E> ActionResponse runAction(Function<A, E> actionFunction, A action) {

        _LOG.info("[\u001B[36m{}\u001B[0m] {}", action.getClass().getSimpleName(), action);

        // Validate sequence number
        validateSequence(action);

        // Execute action function
        E result = actionFunction.apply(action);
        action.setResult(result);

        // Create response
        ActionResponse response = new ActionResponse(action);

        // Increment sequence if action is successful
        sequenceNo ++;

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
        // TODO Move to PlayerService?
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

            // TODO End the round

        }

        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.WATCHING);

        // Move to next player
        Player nextPlayer = players.nextPlayer();
        _LOG.info("{} is now the active player", nextPlayer.getPlayerName());

        eventQueue.queueEvent(new NextPlayerEvent(nextPlayer));
        
    }


    public Card discard(final DiscardAction action) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PLAYING);

        // Resolve card from player hand
        Card handCard = currentPlayer.getHand().findCard(action.getCard());
        if (handCard == null) {

            // Card hasn't been found in hand
            throw new CardNotInHandException(String.format("Card %s not found in player hand", CardEncoder.asShortString(action.getCard())));

        }
        currentPlayer.getHand().removeCard(handCard);

        // Add card to discard pile
        discardPile.discard(handCard);
        
        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.DISCARDED);

        eventQueue.queueEvent(new PlayerDiscardEvent(currentPlayer, handCard));

        return handCard;

    }

    public Card pickupDiscard(final PickupDiscardAction action) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PICKUP);

        // Take card from discard pile
        Card card = discardPile.pickup();
        
        // Add card to player hand
        currentPlayer.getHand().addCard(card);
        
        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);

        eventQueue.queueEvent(new PlayerPickupDiscardEvent(currentPlayer, card));

        return card;

    }
    
    public Card pickupDraw(final PickupDrawAction action) {

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PICKUP);

        // Take card from draw pile
        Card card = drawPile.pickup();
        
        // Add card to player hand
        currentPlayer.getHand().addCard(card);
        
        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);

        eventQueue.queueEvent(new PlayerPickupDrawEvent(currentPlayer));

        return card;

    }
}
