package uk.co.essarsoftware.par.cards;

import java.util.UUID;

/**
 * Implementation of a Joker that has been bound to a card.
 * @author essar
 */
class BoundJoker implements Joker
{

    private final Card boundCard;
    private final UUID cardID;

    /**
     * Instantiate a new BoundJoker class that is bound to the specified card.
     * @param boundCard a card to bind this joker to.
     * @throws IllegalArgumentException if {@code boundCard} is {@code null} or a Joker.
     */
    BoundJoker(Card boundCard) {

        if (boundCard == null) {

            throw new IllegalArgumentException("Cannot bind Joker to null");
        
        }
        if (boundCard.isJoker()) {

            throw new IllegalArgumentException("Cannot bind Joker to a Joker");

        }
        this.cardID = UUID.randomUUID();
        this.boundCard = boundCard;

    }

    /**
     * Get the current bound card.
     * @return a Card.
     */
    protected Card getBoundCard() {

        return boundCard;

    }

    /**
     * Get the card ID.
     * @return a globally unique identifier for this Card instance.
     */
    protected String getCardID() {

        return cardID.toString();

    }

    /**
     * @see Joker#bind(Card)
     * @throws IllegalStateException when called as BoundJoker objects may not be re-bound.
     */
    @Override
    public Joker bind(Card card) {

        throw new IllegalStateException("Joker already bound");

    }

    /**
     * Test if this BoundJoker is equal to another object.
     * A BoundJoker is equal to a {@link Card} if the card is equal to the underlying bound card.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {

        if (o instanceof Card) {

            Card card = (Card) o;
            // Bound joker is equal if it matches the bound card
            return getBoundCard().equals(card);

        }

        return false;

    }

    /**
     * @see Joker#isBound()
     */
    @Override
    public boolean isBound() {

        return true;

    }

    /**
     * @see Joker#isJoker()
     */
    @Override
    public final boolean isJoker() {

        return true;

    }

    /**
     * Return the suit of the underlying bound card.
     * @return the card Suit.
     * @see Joker#getSuit()
     */
    @Override
    public Suit getSuit() {

        return boundCard.getSuit();

    }

    /**
     * Return the value of the underlying bound card.
     * @return the card face value.
     * @see Joker#getValue()
     */
    @Override
    public Value getValue() {

        return boundCard.getValue();

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("BoundJoker@%s[boundCard=%s]", cardID, boundCard);

    }
}
