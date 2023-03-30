package uk.co.essarsoftware.par.cards;

import java.util.Objects;
import java.util.UUID;

/**
 * Default Card implementation.
 * @author essar
 */
class DefaultCard implements Card
{

    private final UUID cardID;
    private final Suit suit;
    private final Value value;

    /**
     * Instantiates a new DefaultCard with the specified suit and value.
     * @param suit the card suit.
     * @param value the card face value.
     */
    DefaultCard(Suit suit, Value value) {

        this.cardID = UUID.randomUUID();
        this.suit = suit;
        this.value = value;

    }

    /**
     * Gets the card ID.
     * @return a globally unique identifier for this Card instance.
     */
    protected String getCardID() {

        return cardID.toString();

    }

    /**
     * @see Card#getCard()
     */
    @Override
    public Suit getSuit() {

        return suit;

    }

    /**
     * @see Card#getValue()
     */
    @Override
    public Value getValue() {

        return value;

    }

    /**
     * Tests if this Card is equal to another Card. Cards are deemed equal if they have the same suits and values.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {

        if (o instanceof Card) {

            Card card = (Card) o;
            // Card matches on suit and value
            return Objects.equals(getSuit(), card.getSuit())
                && Objects.equals(getValue(), card.getValue());

        }

        return false;

    }

    /**
     * @see Card#isJoker()
     */
    @Override
    public final boolean isJoker() {

        return false;
        
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        return String.format("DefaultCard[ID: %s; Suit: %s; Value: %s]", cardID, suit, value);

    }
}
