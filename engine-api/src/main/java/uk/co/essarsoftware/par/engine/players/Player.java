package uk.co.essarsoftware.par.engine.players;

import java.util.Objects;
import java.util.stream.Stream;

import uk.co.essarsoftware.par.cards.Hand;
import uk.co.essarsoftware.par.cards.PenaltyCard;

public class Player
{

    private final Hand hand;
    private final PenaltyCard penaltyCard;
    private final String playerID, playerName;
    private boolean playerDown;
    private PlayerState playerState;

    public Player(String playerID, String playerName) {

        this.playerID = playerID;
        this.playerName = playerName;
        this.hand = new Hand();
        this.penaltyCard = new PenaltyCard();
        this.playerState = PlayerState.INIT;

    }

    public String[] getFlags() {

        return Stream.of(hasPenaltyCard() ? "PC": null, isDown() ? "DOWN" : null)
            .filter(Objects::nonNull)
            .toArray(String[]::new);
    }

    public PenaltyCard getPenaltyCard() {

        return penaltyCard;

    }

    public Hand getHand() {

        return hand;

    }

    public int getHandSize() {
        
        return hand == null ? 0 : hand.size();
    
    }

    public String getPlayerID() {

        return playerID;

    }

    public String getPlayerName() {
        
        return playerName;

    }

    public void setPlayerDown() {

        this.playerDown = true;

    }

    public PlayerState getPlayerState() {
        
        return playerState;

    }
    
    public boolean hasPenaltyCard() {
        
        return penaltyCard.hasPenaltyCard();

    }

    public boolean isDown() {
            
        return playerDown;

    }

    public void setPlayerState(PlayerState playerState) {

        if (playerState == null) {

            throw new IllegalArgumentException("Cannot set player state to null");
        
        }

        this.playerState = playerState;

    }

    @Override
    public String toString() {

        String[] flags = getFlags();
        return String.format("Player[ID: %s; Name: %s; State: %s; Hand: %s; Flags: %s]", getPlayerID(), getPlayerName(), getPlayerState(), getHandSize(), flags.length == 0 ? "none" : String.join(",", flags));

    }
}
