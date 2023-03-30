package uk.co.essarsoftware.par.cards;

/**
 * Implementation of Play that represents a run - three or more card of the same suit and sequential values.
 * @author essar
 */
public class RunPlay extends Play
{

    /**
     * Compares two Cards held in this Run for sorting.
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Card card1, Card card2) {

        // Runs are sorted first by suit, then by value
        if (card1.getSuit() == card2.getSuit()) {

            return card1.getValue().compareTo(card2.getValue());

        }
        return card1.getSuit().compareTo(card2.getSuit());

    }
    
    /**
     * Gets the allowable cards for this Run. If no cards are currently in the run, all cards are allowed.
     * Otherwise allowable cards are of the same suit as existing cards and must have sequential values from
     * the head and tail of the run.
     * @return an array containing the allowed Cards.
     * @see Play#getAllowableCards()
     */
    @Override
    public Card[] getAllowableCards() {

        if (size() == 0) {

             // An empty run will accept any card
            return CardFactory.allCards();

        }

        if (cards.first().getValue() == Value.ACE) {

            return new Card[] { CardFactory.withNextValue(cards.last()) };

        }
        if (cards.last().getValue() == Value.KING) {
        
            return new Card[] { CardFactory.withPreviousValue(cards.first()) };

        }

        return new Card[] { 
            CardFactory.withPreviousValue(cards.first()),
            CardFactory.withNextValue(cards.last())
        };

    }

    /**
     * @return always returns {@code false}.
     * @see Play#isPrial()
     */
    @Override
    public boolean isPrial() {

        return false;

    }

    /**
     * @see Play#isRun()
     */
    @Override
    public boolean isRun() {

        // Is a valid run once it contains three or more cards
        return size() >= 3;
    
    }
}
