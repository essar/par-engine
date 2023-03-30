package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for performing validation operations.
 * @author essar
 */
public class Validator
{

    private static final Logger _LOG = LoggerFactory.getLogger(Hand.class);
    private final HashSet<Card> validCards;

    public Validator(Pack... packs) {

        validCards = new HashSet<>();
        Arrays.stream(packs).flatMap(p -> p.getCardStream()).forEach(validCards::add);
        _LOG.debug("Validator(): {} cards in valid list", validCards.size());

    }

    private boolean isInvalidCard(Card card) {

        // Card should be present in the valid cards list; if it is remove it to prevent dups
        if (!validCards.remove(card)) {

            _LOG.warn("Invalid card found: {}", card);
            return true;

        }
        // Return "isInvalid" status to support stream filtering
        return false;

    }

    private long countInvalidCards(Stream<Card> stream) {

        return stream.parallel().filter(this::isInvalidCard).count();

    }

    public boolean validateCardStream(Stream<Card> stream) {

        long invalidCards = stream.parallel().filter(this::isInvalidCard).count();
        _LOG.debug("validateCardStream(): {} invalid cards found", invalidCards);
        return invalidCards == 0L;

    }

    int validCardsRemaining() {

        return validCards.size();

    }

    public boolean isValid() {

        return validCards.isEmpty();

    }

    public boolean validateContainer(CardContainer container) {

        long invalidCards = countInvalidCards(container.getCardStream());
        _LOG.debug("validateContainer(): {}: {} invalid cards found", container, invalidCards);
        return invalidCards == 0L;

    }

    public boolean validatePile(Pile pile) {

        long invalidCards = countInvalidCards(pile.getCardStream());
        _LOG.debug("validatePile(): {}: {} invalid cards found", pile, invalidCards);
        return invalidCards == 0L;

    }

    /**
     * Do validation
     * @return {@code false}.
     */
    public boolean validate() {

        /*
         * We want to check that all the cards being held belong to packs, and are not duplicated.
         * - Draw pile
         * - Dicard pile
         * - Hands
         * - Plays
         * - Penalty cards
         */

        return false;
    }
}
