package uk.co.essarsoftware.par.cards;

/**
 * Interface representing a Joker playing card.
 * @author essar
 */
public interface Joker extends Card
{

    /**
     * Binds this joker to an underlying card.
     * @param card the card to bind to this joker.
     * @return a new Joker, bound to the specified card.
     */
    public Joker bind(Card card);

    /**
     * Whether this joker is bound to a card.
     * @return {@code true} if the Joker is bound to a card; {@code false} otherwise.
     */
    public boolean isBound();

}
