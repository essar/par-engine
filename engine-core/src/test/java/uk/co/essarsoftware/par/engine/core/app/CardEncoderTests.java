package uk.co.essarsoftware.par.engine.core.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;

/**
 * Test cases for {@link CardEncoder}.
 * @author @essar
 */
public class CardEncoderTests
{

    @Test
    public void testCardEncoderInstantiation() {

        assertNotNull(new CardEncoder(), "Class should be instantiated");

    }

    @Test
    public void testAsCardReturnsCard() {

        assertEquals(Card.as(Suit.CLUBS, Value.ACE), CardEncoder.asCard("1C"), "Expected ACE of CLUBS");
        
    }
    
    @Test
    public void testAsCardReturnsNullForNullString() {

        assertNull(CardEncoder.asCard(null), "Expected a null for null input");
        
    }

    @Test
    public void testAsCardReturnsNullForMalformedString() {

        assertNull(CardEncoder.asCard("notACard"), "Expected a null for a non-card String");

    }

    @Test
    public void testAsCardsReturnsCards() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.SPADES, Value.KING)
        };

        assertArrayEquals(cards, CardEncoder.asCards(new String[] { "1C", "KS" }), "Expected an array of two cards");

    }

    @Test
    public void testAsCardsReturnsNullForNullArray() {

        assertNull(null, "Expected null out for null in");

    }

    @Test
    public void testAsCardsIgnoresNullsInInput() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.SPADES, Value.KING)
        };

        assertArrayEquals(cards, CardEncoder.asCards(new String[] { "1C", null, "KS" }), "Expected an array of two cards");

    }

    @Test
    public void testAsCardsIgnoresInvalidStringsInInput() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.SPADES, Value.KING)
        };

        assertArrayEquals(cards, CardEncoder.asCards(new String[] { "1C", "NotACard", "KS" }), "Expected an array of two cards");

    }

    @Test
    public void testAsCardsReturnsCardsForList() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.SPADES, Value.KING)
        };

        assertArrayEquals(cards, CardEncoder.asCards(new ArrayList<String>(Arrays.asList("1C", "KS"))), "Expected an array of two cards");

    }

    @Test
    public void testAsShortStringReturnsStringForCard() {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        assertEquals("1C", CardEncoder.asShortString(card), "Expected a String of 1C");

    }

    @Test
    public void testAsShortStringReturnsNullStringForNullCard() {

        assertNull(CardEncoder.asShortString((Card) null), "Expected null out for null in");
        
    }

    @Test
    public void testAsShortStringReturnsStringForCardArray() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.SPADES, Value.KING)
        };
        assertEquals("1C,KS", CardEncoder.asShortString(cards), "Expected a String of 1C,KS");

    }

    @Test
    public void testAsShortStringIgnoresNullsForCardArray() {

        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            null,
            Card.as(Suit.SPADES, Value.KING)
        };
        assertEquals("1C,KS", CardEncoder.asShortString(cards), "Expected a String of 1C,KS");

    }

    @Test
    public void testCardSerializerPassesShortString() throws IOException {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        new CardEncoder.CardSerializer().serialize(card, generator, provider);
        verify(generator).writeString("1C");
        
    }

    @Test
    public void testCardDeserializerReturnsExpectedCard() throws IOException {

        Card card = Card.as(Suit.CLUBS, Value.ACE);
        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("1C");
        DeserializationContext context = mock(DeserializationContext.class);
        assertEquals(card, new CardEncoder.CardDeserializer().deserialize(parser, context), "Expected deserializer to return card");
        
    }

    @Test
    public void testCardDeserializerReturnsNullForInvalidCard() throws IOException {

        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("notACard");
        DeserializationContext context = mock(DeserializationContext.class);
        assertNull(new CardEncoder.CardDeserializer().deserialize(parser, context), "Expected deserializer to return card");
        
    }
}
