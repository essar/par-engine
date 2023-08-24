package uk.co.essarsoftware.par.cards;

/**
 * Interface that represents a playing card.
 * @author essar
 */
public interface Card
{

    /**
     * Gets the Cards suit.
     * @return an enum Suit value.
     */
    public Suit getSuit();

    /**
     * Gets the Cards face value.
     * @return an enum Value value.
     */
    public Value getValue();

    /**
     * Specifies if the Card is a Joker.
     * @return {@code true} if the card is a joker; {@code false} otherwise.
     */
    public boolean isJoker();

    /**
     * Create a new simple card object based on a provided suit and value.
     * @param suit the card's suit.
     * @param value the card's face value.
     * @return a Card object.
     */
    public static Card as(Suit suit, Value value) {

        return new DefaultCard(suit, value);

    }
}
