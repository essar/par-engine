package uk.co.essarsoftware.par.cards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ValidatorTests {
    
    @Test
    public void testCreateEmptyValidator() {

        Validator validator = new Validator();
        assertEquals(0, validator.validCardsRemaining(), "No valid cards should remain");
        assertTrue(validator.isValid(), "Validator should be valid");

    }
    
    @Test
    public void testCreateValidatorWithSinglePack() {

        Pack pack = Pack.generatePack();
        Validator validator = new Validator(pack);
        assertEquals(52, validator.validCardsRemaining(), "Should be 52 valid cards");
        assertFalse(validator.isValid(), "Validator should be invalid");

    }

    @Test
    public void testValidateValidPile() {

        Pack pack = Pack.generatePack();
        DrawPile pile = new DrawPile();
        pile.addPack(pack);
        
        Validator validator = new Validator(pack);
        assertTrue(validator.validateContainer(pile.getCardContainer()), "Pile should be valid");

    }

    @Test
    public void testValidateInvalidPile() {

        Pack pack1 = Pack.generatePack();
        Pack pack2 = Pack.generatePack();
        DrawPile pile = new DrawPile();
        pile.addPack(pack1);
        
        Validator validator = new Validator(pack2);
        assertFalse(validator.validateContainer(pile.getCardContainer()), "Pile should be invalid");

    }
}
