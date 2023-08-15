package uk.co.essarsoftware.par.engine.core.app;

import java.util.UUID;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;

public class TestCard implements Card
{

    private final UUID cardId = UUID.randomUUID();
    private final Suit suit;
    private final Value value;

    public TestCard(Card card) {

        this(card.getSuit(), card.getValue());

    }

    public TestCard(final Suit suit, final Value value) {
        this.suit = suit;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestCard) {

            TestCard card = (TestCard) obj;
            return cardId.equals(card.cardId) && getSuit().equals(card.getSuit()) && getValue().equals(card.getValue());
        }
        return false;
    }
    
    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public boolean isJoker() {
        return false;
    }
}

