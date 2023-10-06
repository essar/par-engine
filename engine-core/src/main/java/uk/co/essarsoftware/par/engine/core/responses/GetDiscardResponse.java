package uk.co.essarsoftware.par.engine.core.responses;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.cards.Card;

/**
 * Response class for {@code GET /table/discard}.
 * @author @essar
 */
public class GetDiscardResponse
{

    private final Card discard;

    /**
     * Instantiate response
     * @param discard
     */
    public GetDiscardResponse(Card discard) {

        this.discard = discard;

    }

    /**
     * Get the card at the top of the discard pile.
     * @return the discard card.
     */
    @JsonGetter("card")
    public Card getDiscard() {

        return discard;

    }
}
