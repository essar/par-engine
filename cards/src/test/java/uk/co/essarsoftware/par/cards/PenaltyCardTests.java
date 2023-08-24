package uk.co.essarsoftware.par.cards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PenaltyCardTests {
    
    @Test
    public void testClearPenaltyCardPenaltyCardIsNull() {

        PenaltyCard pCard = new PenaltyCard();
        pCard.setPenaltyCard(TestCardFactory.card());
        pCard.clearPenaltyCard();
        assertNull(pCard.getPenaltyCard(), "Expected penalty card to be null");

    }

    @Test
    public void testGetPenaltyCardIsExpectedCard() {

        PenaltyCard pCard = new PenaltyCard();
        Card card = TestCardFactory.card();
        pCard.setPenaltyCard(card);
        assertEquals(card, pCard.getPenaltyCard(), "Penalty card is not as expected");

    }

    @Test
    public void testHasPenaltyCardWhenPenaltyCardIsSet() {

        PenaltyCard pCard = new PenaltyCard();
        pCard.setPenaltyCard(TestCardFactory.card());
        assertTrue(pCard.hasPenaltyCard(), "Expected hasPenaltyCard to return true");

    }

    @Test
    public void testHasPenaltyCardWhenPenaltyCardNotSet() {

        PenaltyCard pCard = new PenaltyCard();
        assertFalse(pCard.hasPenaltyCard(), "Expected hasPenaltyCard to return false");

    }

    @Test
    public void testSetPenaltyCardThrowsExceptionIfSetToNull() {

        PenaltyCard pCard = new PenaltyCard();
        assertThrows(IllegalArgumentException.class, () -> pCard.setPenaltyCard(null), "Expected an exception trying to set penalty card to null");

    }

    @Test
    public void testSetPenaltyCardThrowsExceptionIfPenaltyCardAlreadySet() {

        PenaltyCard pCard = new PenaltyCard();
        pCard.setPenaltyCard(TestCardFactory.card());
        assertThrows(IllegalStateException.class, () -> pCard.setPenaltyCard(TestCardFactory.card()), "Expected an exception trying to set penalty card when already set");

    }
}
