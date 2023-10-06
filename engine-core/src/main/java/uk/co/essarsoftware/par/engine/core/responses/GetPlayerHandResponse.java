package uk.co.essarsoftware.par.engine.core.responses;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonGetter;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.players.Player;

public class GetPlayerHandResponse
{
    private final Player player;

    public GetPlayerHandResponse(Player player) {

        this.player = player;

    }

    @JsonGetter("hand")
    public Stream<Card> getHand() {

        return player.getHand().getCardsStream();

    }

    @JsonGetter("player_id")
    public String getPlayerID() {

        return player.getPlayerID();
    
    }

    @Override
    public String toString() {

        return String.join(",", getHand()
            .map(CardEncoder::asShortString)
            .toArray(String[]::new));

    }
}
