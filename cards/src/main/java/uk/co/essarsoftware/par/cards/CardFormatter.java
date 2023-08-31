package uk.co.essarsoftware.par.cards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods for formatting {@link Card}s as Strings, as visa versa.
 * @author @essar
 */
public class CardFormatter
{

    private static Map<Suit, String> suitStrings;
    private static Map<Value, String> valueStrings;
    private static Map<String, Suit> stringToSuit;
    private static Map<String, Value> stringToValue;

    static {

        initialiseMaps();

    }

    static void initialiseMaps() {

        suitStrings = Stream.of(Suit.values()).collect(Collectors.toMap(s -> s, CardFormatter::suitAsString));
        valueStrings = Stream.of(Value.values()).collect(Collectors.toMap(v -> v, CardFormatter::valueAsString));
        stringToSuit = Stream.of(Suit.values()).collect(Collectors.toMap(CardFormatter::suitAsString, s -> s));
        stringToValue = Stream.of(Value.values()).collect(Collectors.toMap(CardFormatter::valueAsString, v -> v));

    }

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

    /**
     * Parse a {@link Card} object from the provided String.
     * @param shortStr a String representing the Card in short form.
     * @return the Card, or {@code null} if the String does not represent a known card.
     */
    public static Card asCard(String shortStr) {

        Pattern p = Pattern.compile("^([1-9]|10|[JQK])([CDHS])$");
        Matcher m = p.matcher(shortStr == null ? "" : shortStr);

        if (m.matches()) {

            String valueStr = m.group(1);
            String suitStr = m.group(2);

            return Card.as(stringToSuit.get(suitStr), stringToValue.get(valueStr));

        }

        return null;

    }

    /**
     * Parse {@link Card}s from an array of provided Strings. Strings that cannot be parsed are ignored.
     * @param shortStr an array of Strings representing the Cards in short form.
     * @return an array of Cards.
     */
    public static Card[] asCards(String[] shortStr) {

        return Arrays.stream(shortStr)
            .map(CardFormatter::asCard)
            .filter(Objects::nonNull)
            .toArray(Card[]::new);

    }

    /**
     * Parse {@link Card}s from a List of provided Strings. Strings that cannot be parsed are ignored.
     * @param shortStr an array of Strings representing the Cards in short form.
     * @return an array of Cards.
     */
    public static Card[] asCards(List<String> shortStr) {

        return shortStr.stream()
            .map(CardFormatter::asCard)
            .filter(Objects::nonNull)
            .toArray(Card[]::new);

    }

    /**
     * Format a {@link Card} as a String in short form.
     * @param card the Card to format.
     * @return a short form String representation of the Card.
     */
    public static String asShortString(Card card) {
        
        return card.isJoker() ? "*J" : String.format("%s%s", valueStrings.get(card.getValue()), suitStrings.get(card.getSuit()));
        
    }

    /**
     * Format a {@link CardContainer} of {@link Card}s as a String in short form.
     * @param card the CardContainer containing Cards to format.
     * @return a short form String representation of the Card.
     */
    public static String asShortString(CardContainer cards) {

        return String.join(",", cards.getCardStream().map(CardFormatter::asShortString).toList());

    }

    /**
     * Format an array of {@link Card}s as a String in short form.
     * @param card the Cards to format.
     * @return a short form String representation of the Card.
     */
    public static String asShortString(Card[] cards) {

        return String.join(",", Arrays.stream(cards).map(CardFormatter::asShortString).toList());

    }
}
