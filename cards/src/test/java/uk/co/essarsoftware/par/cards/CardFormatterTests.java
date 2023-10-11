package uk.co.essarsoftware.par.cards;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CardFormatter}.
 * @author @essar
 */
public class CardFormatterTests
{

    @BeforeEach
    public void initMaps() {

        CardFormatter.initialiseMaps();

    }

    @Test
    public void testCardFormatterInstantiation() {

        assertNotNull(new CardFormatter(), "Class should be instantiated");

    }

    @Test
    public void testAsCardReturnsExpectedCardForValidString() {

        String str = "10C";
        Card card = Card.as(Suit.CLUBS, Value.TEN);
        assertEquals(card, CardFormatter.asCard(str), "Expected correct card");
        
    }

    @Test
    public void testAsCardReturnsExpectedCardForAceValue() {

        String str = "1C";
        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertEquals(card, CardFormatter.asCard(str), "Expected correct card");
        
    }

    @Test
    public void testAsCardReturnsNullForInvalidSuit() {

        String str = "3B";
        assertNull(CardFormatter.asCard(str), "Expected null response");

    }

    @Test
    public void testAsCardReturnsNullForInvalidValue() {

        String str = "11C";
        assertNull(CardFormatter.asCard(str), "Expected null response");

    }

    @Test
    public void testAsCardReturnsNullForNullInput() {

        assertNull(CardFormatter.asCard(null), "Expected null response");
        
    }

    @Test
    public void testAsCardsReturnsCardArrayForValidCardArray() {

        String[] strs = new String[] { "2C", "QS" };
        Card[] cards = new Card[] { Card.as(Suit.CLUBS, Value.TWO), Card.as(Suit.SPADES, Value.QUEEN)};

        assertArrayEquals(cards, CardFormatter.asCards(strs), "Expected valid array of Cards");

    }

    @Test
    public void testAsCardsReturnsCardArrayForValidCardList() {

        List<String> strs = Arrays.asList(new String[] { "2C", "QS" });
        Card[] cards = new Card[] { Card.as(Suit.CLUBS, Value.TWO), Card.as(Suit.SPADES, Value.QUEEN)};

        assertArrayEquals(cards, CardFormatter.asCards(strs), "Expected valid array of Cards");

    }

    @Test
    public void testAsCardsReturnsCardArrayForMixedCardArray() {

        String[] strs = new String[] { "2C", "QS", "BE" };
        Card[] cards = new Card[] { Card.as(Suit.CLUBS, Value.TWO), Card.as(Suit.SPADES, Value.QUEEN)};

        assertArrayEquals(cards, CardFormatter.asCards(strs), "Expected valid array of Cards");
        
    }

    @Test
    public void testAsShortStringReturnsExpectedStringForJoker() {

        Card card = new UnboundJoker();
        String str = "*J";
        assertEquals(str, CardFormatter.asShortString(card), "Expected joker string");
    
    }

    @Test
    public void testAsShortStringReturnsExpectedStringForCard() {

        Card card = Card.as(Suit.DIAMONDS, Value.EIGHT);
        String str = "8D";
        assertEquals(str, CardFormatter.asShortString(card), "Expected card string");
    
    }

    @Test
    public void testAsShortStringReturnsNullForNullCard() {

        assertNull(CardFormatter.asShortString((Card) null), "Expected null");
    
    }

    @Test
    public void testAsShortStringReturnsExpectedStringArrayForCardArray() {

        Card[] cards = new Card[] { Card.as(Suit.DIAMONDS, Value.EIGHT), Card.as(Suit.HEARTS, Value.ACE) };
        String str = "8D,1H";
        assertEquals(str, CardFormatter.asShortString(cards), "Expected card string");
    
    }

    @Test
    public void testAsShortStringReturnsNullForNullCardArray() {

        assertNull(CardFormatter.asShortString((Card[]) null), "Expected null");
    
    }

    @Test
    public void testAsShortStringReturnsExpectedStringArrayForCardContainer() {

        CardContainer cards = new CardContainer() {
            @Override
            public Stream<Card> getCardStream() {
                return Arrays.stream(new Card[] { Card.as(Suit.DIAMONDS, Value.EIGHT), Card.as(Suit.HEARTS, Value.ACE) });
            }
        };
        String str = "8D,1H";
        assertEquals(str, CardFormatter.asShortString(cards), "Expected card string");
    
    }

    @Test
    public void testAsShortStringReturnsNullForNullCardContainer() {

        assertNull(CardFormatter.asShortString((CardContainer) null), "Expected null");
    
    }
    
}
