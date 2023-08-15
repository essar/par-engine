package uk.co.essarsoftware.par.engine.core.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;

@JsonComponent
public class CardEncoder
{

    private static Map<Suit, String> suitStrings =
        Stream.of(Suit.values()).collect(Collectors.toMap(s -> s, CardEncoder::suitAsString));
    private static Map<Value, String> valueStrings =
        Stream.of(Value.values()).collect(Collectors.toMap(v -> v, CardEncoder::valueAsString));
    private static Map<String, Suit> stringToSuit =
        Stream.of(Suit.values()).collect(Collectors.toMap(CardEncoder::suitAsString, s -> s));
    private static Map<String, Value> stringToValue =
        Stream.of(Value.values()).collect(Collectors.toMap(CardEncoder::valueAsString, v -> v));

    private static String suitAsString(Suit suit) {

        return suit.toString().substring(0, 1).toUpperCase();

    }

    private static String valueAsString(Value value) {

        switch (value) {
            case JACK:
            case QUEEN:
            case KING:
                return value.toString().substring(0, 1).toUpperCase();
            default:
                return Integer.toString(Arrays.asList(Value.values()).indexOf(value) + 1);
        }
    }

    public static Card asCard(String shortStr) {

        Pattern p = Pattern.compile("^([2-9]|10|[AJQK])([CDHS])$");
        Matcher m = p.matcher(shortStr);

        if (m.matches()) {

            String valueStr = m.group(1);
            String suitStr = m.group(2);

            return Card.as(stringToSuit.get(suitStr), stringToValue.get(valueStr));

        }
        
        if (shortStr.length() == 2) {

            String valueStr = shortStr.substring(0, 1);
            String suitStr = shortStr.substring(1, 2);

            return Card.as(stringToSuit.get(suitStr), stringToValue.get(valueStr));

        }

        return null;

    }

    public static Card[] asCards(String[] shortStr) {

        return Arrays.stream(shortStr).map(CardEncoder::asCard).toArray(Card[]::new);

    }

    public static Card[] asCards(ArrayList<String> shortStr) {

        return shortStr.stream().map(CardEncoder::asCard).toArray(Card[]::new);

    }

    public static String asShortString(Card card) {
        
        return card.isJoker() ? "*J" : String.format("%s%s", valueStrings.get(card.getValue()), suitStrings.get(card.getSuit()));
        
    }

    public static String asShortString(Card[] cards) {

        return String.join(",", Arrays.stream(cards).map(CardEncoder::asShortString).toList());

    }

    public static class CardSerializer extends JsonSerializer<Card>
    {
        @Override
        public void serialize(Card card, JsonGenerator json, SerializerProvider provider) throws IOException {
            
            json.writeString(asShortString(card));

        }
    }

    public static class CardDeserializer extends JsonDeserializer<Card>
    {
        @Override
        public Card deserialize(JsonParser json, DeserializationContext context) throws IOException, JacksonException {
            
            return asCard(json.getText());
            
        }
    }
}
