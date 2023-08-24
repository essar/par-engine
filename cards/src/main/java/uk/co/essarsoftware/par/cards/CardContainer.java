package uk.co.essarsoftware.par.cards;

import java.util.stream.Stream;

/**
 * Interface for objects that hold Cards.
 * @author essar
 */
interface CardContainer {

    /**
     * Retrieve a Stream that contains all the cards held in this CardContainer.
     * @return a Stream of Cards.
     */
    Stream<Card> getCardStream();
    
}
