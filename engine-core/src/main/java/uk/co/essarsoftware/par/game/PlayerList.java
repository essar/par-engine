package uk.co.essarsoftware.par.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class PlayerList extends LinkedList<Player>
{

    private final PlayerListPointer playerPointer = new PlayerListPointer();

    public boolean containsPlayer(final String playerID) {

        return stream().anyMatch(p -> Objects.equals(p.getPlayerID(), playerID));

    }

    public Player getCurrentPlayer() {

        // Current player is referenced by the current index pointer
        return  isEmpty() ? null : get(playerPointer.currentIndex());

    }

    public Player getDealer() {

        // The dealer is always at the back of the list
        return isEmpty() ? null : getLast();

    }

    public Player getNextPlayer() {

        return isEmpty() ? null : get(playerPointer.nextIndex());

    }

    public Player toNextPlayer() {

        // Increment the player pointer
        playerPointer.next();

        return getCurrentPlayer();

    }

    public Player newRound() {

        // Move first player to end of list - they become the dealer
        addLast(removeFirst());

        // Reset the current player
        playerPointer.reset();

        return getCurrentPlayer();

    }

    public void shufflePlayers() {

        // Shuffle the players in the list
        Collections.shuffle(this);

        // Reset the current player
        playerPointer.reset();

    }

    private class PlayerListPointer
    {
        private int playerIndex = 0;

        int currentIndex() {

            return playerIndex;

        }

        synchronized int nextIndex() {

            if (playerIndex + 1 >= size()) {

                return 0;
            
            }

            return playerIndex + 1;

        }

        synchronized void next() {

            playerIndex = nextIndex();

        }

        synchronized void reset() {

            playerIndex = 0;

        }
    }
}
