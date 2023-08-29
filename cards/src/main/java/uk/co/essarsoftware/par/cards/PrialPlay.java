package uk.co.essarsoftware.par.cards;

/**
 * Implementation of Play that represents a prial - three or more cards of the same value.
 * @author essar
 */
public class PrialPlay extends Play
{

    /**
     * Compare two Cards held in this Prial for sorting.
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Card card1, Card card2) {

        // Prials are simply sorted by suit
        return card1.getSuit().compareTo(card2.getSuit());

    }

    /**
     * Get the allowable cards for this Prial. If no cards are currently in the run, all cards are allowed.
     * Otherwise allowable cards are of the same value as existing cards.
     * @return an array containing the allowed Cards.
     * @see Play#getAllowableCards()
     */
    @Override
    public Card[] getAllowableCards() {

        if (size() == 0) {

            // An empty prial will accept any card
            return CardFactory.allCards();

        }

        // Get the value of the first card in the prial
        Value value = cards.first().getValue();
        // Prials will accept any cards of the same value
        return CardFactory.forValue(value);

    }

    /**
     * @see Play#isPrial()
     */
    @Override
    public boolean isPrial() {

        // Is a valid prial once it has three or more cards
        return size() >= 3;

    }

    /**
     * @return always returns {@code false}.
     * @see Play#isRun()
     */
    @Override
    public boolean isRun() {

        return false;
        
    }
}
