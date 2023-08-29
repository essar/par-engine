package uk.co.essarsoftware.par.cards;

import java.util.Collections;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * Represents a pile of undrawn cards, which can taken from (pickup).
 * @author essar
 */
@Component
public class DrawPile extends Pile
{

    /**
     * Shuffle the DrawPile based on a known seed.
     * @param seed a long used to seed the shuffle.
     */
    void shuffle(final Long seed) {

        synchronized (cards) {
        
            Collections.shuffle(cards, new Random(seed));

        }
    }

    /**
     * Add a Pack of Cards to the DrawPile.
     * @param pack a Pack of Cards.
     */
    public void addPack(Pack pack) {

        synchronized (cards) {
        
            pack.getCardStream().forEach(cards::add);

        }
    }

    /**
     * Shuffle the DrawPile.
     */
    public void shuffle() {

        synchronized (cards) {
        
            Collections.shuffle(cards);

        }
    }
}
