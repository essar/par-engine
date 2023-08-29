package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardFormatter
{

    private static Map<Suit, String> suitStrings =
        Stream.of(Suit.values()).collect(Collectors.toMap(s -> s, CardFormatter::suitAsString));
    private static Map<Value, String> valueStrings =
        Stream.of(Value.values()).collect(Collectors.toMap(v -> v, CardFormatter::valueAsString));
    private static Map<String, Suit> stringToSuit =
        Stream.of(Suit.values()).collect(Collectors.toMap(CardFormatter::suitAsString, s -> s));
    private static Map<String, Value> stringToValue =
        Stream.of(Value.values()).collect(Collectors.toMap(CardFormatter::valueAsString, v -> v));

    /**
     * Convert a Suit name to a simple String.
     * @param suit the Suit to convert
     * @return a String representing the Suit
     */
    private static String suitAsString(Suit suit) {

        return suit.toString().substring(0, 1).toUpperCase();

    }
    
    /**
     * Convert a Value name to a simple String.
     * @param suit the Value to convert
     * @return a String representing the Value
     */
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

        return Arrays.stream(shortStr).map(CardFormatter::asCard).toArray(Card[]::new);

    }

    public static Card[] asCards(List<String> shortStr) {

        return shortStr.stream().map(CardFormatter::asCard).toArray(Card[]::new);

    }

    public static String asShortString(Card card) {
        
        return card.isJoker() ? "*J" : String.format("%s%s", valueStrings.get(card.getValue()), suitStrings.get(card.getSuit()));
        
    }

    public static String asShortString(CardContainer cards) {

        return String.join(",", cards.getCardStream().map(CardFormatter::asShortString).toList());

    }

    public static String asShortString(Card[] cards) {

        return String.join(",", Arrays.stream(cards).map(CardFormatter::asShortString).toList());

    }
}