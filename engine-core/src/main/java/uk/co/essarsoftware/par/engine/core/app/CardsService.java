package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.DiscardPile;
import uk.co.essarsoftware.par.cards.DrawPile;
import uk.co.essarsoftware.par.cards.Pack;
import uk.co.essarsoftware.par.engine.core.app.players.PlayersService;

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
}
