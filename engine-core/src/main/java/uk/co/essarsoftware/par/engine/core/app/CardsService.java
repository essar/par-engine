package uk.co.essarsoftware.par.engine.core.app;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.Pack;
import uk.co.essarsoftware.par.engine.core.exceptions.CardNotInHandException;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayersService;

@Service
public class CardsService
{
    @Autowired
    private DiscardPile discardPile;

    @Autowired
    private DrawPile drawPile;

    @Autowired
    private PlayersService players;

    final int HAND_SIZE = 11;
        
    public void dealHands() {

        // Clear hands
        players.getPlayersStream().forEach(p -> p.getHand().clear());

        // Shuffle deck
        drawPile.shuffle();

        // Deal cards
        for (int i = 0; i < HAND_SIZE; i ++) {

            players.getPlayersStream()
                .forEach(p -> p.getHand().addCard(drawPile.pickup()));

        }
        // Flip the top draw card to discard pile
        discardPile.discard(drawPile.pickup());

    }

    public void initPiles() {

        // Clear existing piles
        drawPile.clear();
        discardPile.clear();

        // Add packs to the pile
        Pack p = Pack.generatePackWithJokers();
        drawPile.addPack(p);

    }

    public Card getDiscard() {

        return discardPile.getDiscard();
        
    }

    public Card pickupDiscard() {

        return discardPile.pickup();

    }

    public Card pickupDraw() {

        return drawPile.pickup();

    }

    public Card[] resolveCards(Player player, Card... cards) {

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
            .filter(Objects::nonNull)
            .toArray(Card[]::new);

        return resolvedCards;
        
    }
}
