package uk.co.essarsoftware.par.engine.core.app;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.CardFormatter;

@JsonComponent
public class CardEncoder
{

    public static Card asCard(String shortStr) {

        return CardFormatter.asCard(shortStr);

    }

    public static Card[] asCards(String[] shortStrs) {

        return CardFormatter.asCards(shortStrs);

    }

    public static Card[] asCards(ArrayList<String> shortStrs) {

        return CardFormatter.asCards(shortStrs);

    }

    public static String asShortString(Card card) {
        
        return CardFormatter.asShortString(card);
        
    }

    public static String asShortString(Card[] cards) {

        return CardFormatter.asShortString(cards);

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
