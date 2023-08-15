package uk.co.essarsoftware.par.engine.core.app.actions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.cards.PrialPlay;
import uk.co.essarsoftware.par.cards.RunPlay;
import uk.co.essarsoftware.par.cards.Suit;
import uk.co.essarsoftware.par.cards.Value;
import uk.co.essarsoftware.par.engine.core.app.InvalidPlayException;
import uk.co.essarsoftware.par.game.PlaySet;
import uk.co.essarsoftware.par.game.Player;

/**
 * Tests for {@link ActionService}.
 * @author essar
 */
public class PlaysServiceTests
{

    private static Card[] invalidPlay() {
        return new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.DIAMONDS, Value.TWO),
            Card.as(Suit.HEARTS, Value.THREE)
        };
    }

    private static Card[] validPrial() {
        return new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.DIAMONDS, Value.ACE),
            Card.as(Suit.HEARTS, Value.ACE)
        };
    }

    private static Card[] validRun() {
        return new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.CLUBS, Value.TWO),
            Card.as(Suit.CLUBS, Value.THREE)
        };
    }

    @Test
    public void testBuildPlayValidPrial() {

        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        Play play = playsSvc.buildPlay(new PrialPlay(), validPrial());

        assertTrue(play.isPrial(), "Play is a valid Prial");

    }

    @Test
    public void testBuildPlayValidRun() {

        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        Play play = playsSvc.buildPlay(new RunPlay(), validRun());

        assertTrue(play.isRun(), "Play is a valid Run");

    }

    @Test
    public void testBuildPlayOnExistingPlayReturnsUnchangedPlay() {
    
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        Play existingPlay = new PrialPlay();
        existingPlay.addCard(validPrial()[0]);

        Play play = playsSvc.buildPlay(existingPlay, validRun());

        assertArrayEquals(existingPlay.getCards(), play.getCards(), "Play is returned unchanged");
    
    }

    @Test
    public void testBuildPlayInvalidPlayReturnsPlayNotPrialNorRun() {
    
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        Card[] invalidPlay = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.DIAMONDS, Value.TWO),
            Card.as(Suit.HEARTS, Value.THREE)
        };

        Play play = playsSvc.buildPlay(new PrialPlay(), invalidPlay);

        assertFalse(play.isPrial(), "Play is not a valid Prial");
        assertFalse(play.isRun(), "Play is not a valid Run");

    }

    @Test
    public void testBuildPlayForPlayerValidPrial() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        // Add Play to playset
        plays.put(player, new Play[] { new PrialPlay() });

        Play play = playsSvc.buildPlayForPlayer(player, validPrial());

        assertTrue(play.isPrial(), "Play is valid Prial");

    }

    @Test
    public void testBuildPlayForPlayerValidRun() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        // Add Play to playset
        plays.put(player, new Play[] { new RunPlay() });

        Play play = playsSvc.buildPlayForPlayer(player, validRun());

        assertTrue(play.isRun(), "Play is valid Run");

    }

    @Test
    public void testBuildPlayForPlayerEmptyPlaysetThrowsException() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        // Add Play to playset
        plays.put(player, new Play[0]);

        assertThrows(InvalidPlayException.class, () -> playsSvc.buildPlayForPlayer(player, validPrial()), "Using empty playset should throw exception");

    }

    @Test
    public void testBuildPlayForPlayerUnknownPlayerThrowsException() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);

        // Add Play to playset
        plays.put(player, new Play[] { new RunPlay() });

        assertThrows(InvalidPlayException.class, () -> playsSvc.buildPlayForPlayer(player, validPrial()), "Using empty playset should throw exception");

    }

    @Test
    public void testBuildPlayForPlayerFewerThanThreeCardsThrowsException() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);
        Card[] cards = Arrays.copyOfRange(validPrial(), 0, 2);

        // Add Play to playset
        plays.put(player, new Play[] { new PrialPlay() });

        assertThrows(InvalidPlayException.class, () -> playsSvc.buildPlayForPlayer(player, cards), "Building Play with fewer than three cards should throw exception");

    }

    @Test
    public void testBuildPlayForPlayerMoreThanThreeCardsThrowsException() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);
        Card[] cards = new Card[] {
            validPrial()[0],
            validPrial()[1],
            validPrial()[2],
            Card.as(Suit.CLUBS, Value.FOUR)
        };
        
        // Add Play to playset
        plays.put(player, new Play[] { new PrialPlay() });

        assertThrows(InvalidPlayException.class, () -> playsSvc.buildPlayForPlayer(player, cards), "Building Play with more than three cards should throw exception");

    }

    @Test
    public void testBuildPlayForInvalidPlayThrowsException() {

        Player player = new Player("test", "Test Player");
        PlaySet plays = new PlaySet();
        PlaysService playsSvc = new PlaysService(plays);
        
        // Add Play to playset
        plays.put(player, new Play[] { new PrialPlay() });
        
        Card[] cards = new Card[] {
            Card.as(Suit.CLUBS, Value.ACE),
            Card.as(Suit.DIAMONDS, Value.TWO),
            Card.as(Suit.HEARTS, Value.THREE)
        };

        assertThrows(InvalidPlayException.class, () -> playsSvc.buildPlayForPlayer(player, cards), "Building Play invalid cards should throw exception");

    }

    @Test
    public void testIsValidPlayValidPrial() {

        PlaysService playsSvc = new PlaysService(new PlaySet());

        // Create play and add cards
        Play play = new PrialPlay();
        Arrays.stream(validPrial()).forEach(play::addCard);

        assertTrue(playsSvc.isValidPlay(play), "Play is valid Prial");

    }

    @Test
    public void testIsValidPlayValidRun() {

        PlaysService playsSvc = new PlaysService(new PlaySet());

        // Create play and add cards
        Play play = new RunPlay();
        Arrays.stream(validRun()).forEach(play::addCard);

        assertTrue(playsSvc.isValidPlay(play), "Play is valid Run");
        
    }

    @Test
    public void testIsValidNullIsNotValidPlay() {

        PlaysService playsSvc = new PlaysService(new PlaySet());

        assertFalse(playsSvc.isValidPlay(null), "Null object is not a valid Play");
        
    }

    @Test
    public void testIsValidPlayEmptyPlayIsNotValidPlay() {

        PlaysService playsSvc = new PlaysService(new PlaySet());

        // Create play and add cards
        Play play = new PrialPlay();

        assertFalse(playsSvc.isValidPlay(play), "Empty Play is not a valid Play");

    }
}
