package uk.co.essarsoftware.par.engine.core.app.actions;

import java.util.Arrays;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.actions.Action;
import uk.co.essarsoftware.par.engine.actions.DiscardActionHandler.DiscardAction;
import uk.co.essarsoftware.par.engine.actions.PickupDiscardActionHandler.PickupDiscardAction;
import uk.co.essarsoftware.par.engine.actions.PickupDrawActionHandler.PickupDrawAction;
import uk.co.essarsoftware.par.engine.actions.PlayCardsActionHandler.PlayCardsAction;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.core.app.CardNotInHandException;
import uk.co.essarsoftware.par.engine.core.events.NextPlayerEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerDiscardEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerPickupDiscardEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerPickupDrawEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerPlayCardsEvent;
import uk.co.essarsoftware.par.engine.core.events.PlayerStateChangeEvent;
import uk.co.essarsoftware.par.engine.core.events.RoundStartedEvent;
import uk.co.essarsoftware.par.engine.events.EngineEventQueue;
import uk.co.essarsoftware.par.engine.events.EventProcessor;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerState;
import uk.co.essarsoftware.par.engine.players.PlayersService;
import uk.co.essarsoftware.par.engine.plays.PlaysService;

@Service
public class ActionsService
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(ActionsService.class);

    private final ActionSequencer actionSequencer;
    private final EventProcessor eventProcessor;
    private final EngineEventQueue eventQueue;
    private final PlaysService plays;
    private final PlayersService players;

    public ActionsService(final EngineEventQueue eventQueue, final EventProcessor eventProcessor, final PlayersService players, final PlaysService playsSvc, final ActionSequencer actionSequencer, final DrawPile drawPile, final DiscardPile discardPile) {

        this.actionSequencer = actionSequencer;
        this.eventProcessor = eventProcessor;
        this.eventQueue = eventQueue;
        this.players = players;
        this.plays = playsSvc;

    }

    Card resolveCard(Player player, Card card) {

        Card[] cards = resolveCards(player, card);
        if (cards.length > 0) {

            return cards[0];

        }
        return null;

    }

    Card[] resolveCards(Player player, Card... cards) {

        // Create temporary hand and add all cards from the player's hand
        Hand tempHand = new Hand();
        player.getHand().getCardsStream().forEach(tempHand::addCard);

        // Resolve cards from player hand
        Card[] resolvedCards = Arrays.stream(cards)
            .map(c -> {
                Card handCard = tempHand.findCard(c);
                if (handCard == null) {

                    // Card hasn't been found in hand
                    throw new CardNotInHandException(String.format("Card %s not found in player hand", CardEncoder.asShortString(c)));

                }
                // Remove from the temporary hand to prevent duplicate selection
                tempHand.removeCard(handCard);
                return handCard;
            })
            .toArray(Card[]::new);

        return resolvedCards;
        
    }

    @PostConstruct
    public void registerProcessors() {

        // Activate player processor
        eventProcessor.registerProcessor(NextPlayerEvent.class, event -> { activate(event.getPlayer()); });

        // Game started processor
        eventProcessor.registerProcessor(RoundStartedEvent.class, event -> { activate(event.getCurrentPlayer()); });

        // Player end turn processor
        // TODO Move to PlayerService?
        eventProcessor.registerProcessor(PlayerStateChangeEvent.class, event -> event.getNewState() == PlayerState.DISCARDED, event -> { endTurn(event.getPlayer()); });
        
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
        _LOGGER.info("{} is now the active player", nextPlayer.getPlayerName());

        eventQueue.queueEvent(new NextPlayerEvent(nextPlayer));
        
    }


    ActionResponse createActionResponse(Action<?> action) {

        ActionResponse response = new ActionResponse(action);

        // Increment sequence if action is successful
        int nextSequenceNo = actionSequencer.nextSequence();

        // Inject next sequence number into response
        response.setNextActionSequence(nextSequenceNo);

        return response;
        
    }

    public DiscardAction discard(DiscardAction action) {

        // Validate action
        actionSequencer.validateSequence(action);

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PLAYING);
        _LOGGER.debug("Performing action with current player: {}", currentPlayer);

        // Resolve card argument
        Card resolvedCard = resolveCard(currentPlayer, action.getCard());

        // Run action
        Card discardedCard = action.discard(currentPlayer, resolvedCard);

        // Change player state
        players.setPlayerState(currentPlayer, PlayerState.DISCARDED);
        _LOGGER.debug("Player state set to {}", currentPlayer.getPlayerState());

        eventQueue.queueEvent(new PlayerDiscardEvent(currentPlayer, discardedCard));

        return action;

    }
    
    public PickupDrawAction pickupDraw(PickupDrawAction action) {

        // Validate action
        actionSequencer.validateSequence(action);

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PICKUP);
        _LOGGER.debug("Performing action with current player: {}", currentPlayer);

        // Run action
        action.pickupDraw(currentPlayer);

        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);
        _LOGGER.debug("Player state set to {}", currentPlayer.getPlayerState());
        
        eventQueue.queueEvent(new PlayerPickupDrawEvent(currentPlayer));

        return action;

    }
    
    public PickupDiscardAction pickupDiscard(PickupDiscardAction action) {

        // Validate action
        actionSequencer.validateSequence(action);

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PICKUP);
        _LOGGER.debug("Performing action with current player: {}", currentPlayer);

        // Run action
        Card card = action.pickupDiscard(currentPlayer);

        // Change player state
        players.setPlayerState(currentPlayer, currentPlayer.isDown() ? PlayerState.PEGGING : PlayerState.PLAYING);
        _LOGGER.debug("Player state set to {}", currentPlayer.getPlayerState());
        
        eventQueue.queueEvent(new PlayerPickupDiscardEvent(currentPlayer, card));

        return action;

    }

    public PlayCardsAction playCards(final PlayCardsAction action) {

        // Validate action
        actionSequencer.validateSequence(action);

        // Check specified player is current player and in correct state
        Player currentPlayer = players.validateIsCurrentPlayerAndInState(action.getPlayerID(), PlayerState.PLAYING);

        // Get cards and resolve any jokers
        // TODO Resolve jokers
        Card[] resolvedCards = resolveCards(currentPlayer, action.getCards());

        // Run action
        Play play = action.playCards(currentPlayer, resolvedCards);

        // Change player state
        if (plays.hasAvailablePlaysRemaining(currentPlayer)) {

            // Player has available plays remaining
            players.setPlayerState(currentPlayer, PlayerState.PLAYING);

        } else {
        
            // Player has played all of their plays so now has to discard
            players.setPlayerState(currentPlayer, PlayerState.DISCARD);

        }

        eventQueue.queueEvent(new PlayerPlayCardsEvent(currentPlayer, play));

        return action;

    }
}
