package uk.co.essarsoftware.par.engine.core.app.plays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.CardFormatter;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.cards.PrialPlay;
import uk.co.essarsoftware.par.cards.RunPlay;
import uk.co.essarsoftware.par.engine.core.app.InvalidPlayException;
import uk.co.essarsoftware.par.engine.game.Round;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.players.PlayerList;
import uk.co.essarsoftware.par.engine.plays.PlaySet;
import uk.co.essarsoftware.par.engine.plays.PlaysService;


/**
 * Implementation of {@link PlaysService}, providing operations over Play objects.
 * @author @essar
 */
@Service
public class PlaysServiceImpl implements PlaysService
{

    private static final Logger _LOGGER = LoggerFactory.getLogger(PlaysServiceImpl.class);

    private final PlayerList players;
    private final PlaySet plays;

    /**
     * Instantiate a new {@link PlaysServiceImpl} object.
     * @param plays a set of {@link Play}s on the table.
     * @param players a list of {@link Player}s in the gane.
     */
    public PlaysServiceImpl(final PlaySet plays, final PlayerList players) {

        this.players = players;
        this.plays = plays;

    }

    /**
     * Adds a {@link Card}} to an existing {@link Play}.
     * @param play the Play to add a card to.
     * @param card the Card to add.
     */
    private void addCardToPlay(Play play, Card card) {

        _LOGGER.trace("addCardToPlay({}, {})", play, card);
        try {
        
            play.addCard(card);
            _LOGGER.debug("Added {} to {} {}", CardFormatter.asShortString(card), play.getClass().getSimpleName(), CardFormatter.asShortString(play.getCards()));
        
        } catch (IllegalArgumentException ie) {

            // Log and continue
            _LOGGER.debug("Card {} cannot be added to {} {}", CardFormatter.asShortString(card), play.getClass().getSimpleName(), CardFormatter.asShortString(play.getCards()));

        }
    }

    /**
     * Creates {@link Play}s for a {@link Player}; adds these to the current {@link PlaySet}.
     * @param player the owner of the Plays.
     * @param prialCount the number of {@link PrialPlay}s to create.
     * @param runCount the number of {@link RunPlay}s to create.
     */
    private void createPlaysForPlayer(Player player, int prialCount, int runCount) {

        _LOGGER.trace("createPlaysForPlayer({}, {}, {})", player, prialCount, runCount);
        ArrayList<Play> playerPlays = new ArrayList<>();
        for (int i = 0; i < prialCount; i ++) {
            playerPlays.add(new PrialPlay());
        }
        for (int i = 0; i < runCount; i ++) {
            playerPlays.add(new RunPlay());
        }
        plays.put(player, playerPlays.toArray(new Play[0]));
        _LOGGER.debug("Created {} play(s) for {}", playerPlays.size(), player.getPlayerName());

    }

    /**
     * Builds into a {@link Play}, using the provided {@link Card}s.
     * @param play the Play to build into.
     * @param cards an array of Cards to build into a Play.
     * @return the Play that has been built.
     */
    Play buildPlay(final Play play, final Card[] cards) {

        _LOGGER.trace("buildPlay({}, {})", play, Arrays.toString(cards));
        // Check if play is empty and refuse otherwise
        if (play.size() != 0) {

            _LOGGER.warn("Not adding cards to existing play");
            return play;

        }

        // For each card provided, sort them by the order specified by the play and try to add to the play and check the outcome
        Arrays.stream(cards)
            .sorted(play)
            .forEach(c -> addCardToPlay(play, c));
        _LOGGER.debug("{} contains {} card(s) after initialisation", play.getClass().getSimpleName(), play.size());

        // Clean up if we've not completed a valid play
        if (! isValidPlay(play)) {

            play.reset();
            _LOGGER.debug("Play reset as did not build it successfully");

        }

        return play;

    }

    /**
     * Checks if the specified Play is valid.
     * @param play the Play to check.
     * @return {@code true} if the Play is complete and valid; {@code false} otherwise.
     */
    boolean isValidPlay(Play play) {

        return Objects.nonNull(play) && (play.isPrial() || play.isRun());

    }

    /**
     * @see PlaysService#buildPlayForPlayer(Player, Card[])
     */
    @Override
    public Play buildPlayForPlayer(final Player player, final Card[] cards) {

        _LOGGER.trace("buildPlayForPlayer({}, {})", player, Arrays.toString(cards));
        // Check we have three cards
        if (cards.length != 3) {

            throw new InvalidPlayException("Play must contain three cards");

        }

        Play play;
        synchronized (plays) {
        
            // Try and find an available play
            play = Arrays.stream(plays.getAvailablePlays(player))
                .map(p -> buildPlay(p, cards))
                .filter(this::isValidPlay)
                .findAny()
                .orElseThrow(() -> new InvalidPlayException(String.format("Cards %s cannot be used to build a valid play", CardFormatter.asShortString(cards))));
            _LOGGER.debug("Created {} from {} for {}", play.getClass().getSimpleName(), CardFormatter.asShortString(cards), player.getPlayerName());
        
        }

        return play;

    }

    /**
     * @see PlaysService#hasAvailablePlaysRemaining(Player)
     */
    @Override
    public boolean hasAvailablePlaysRemaining(Player player) {

        return plays.getAvailablePlays(player).length > 0;

    }

    /**
     * @see PlaysService#initPlaysForRound(Round)
     */
    @Override
    public void initPlaysForRound(final Round round) {

        synchronized (plays) {
            // Remove any existing plays
            plays.clear();

            // Create plays according to round
            switch (round) {
                case PP:
                    players.forEach(player -> createPlaysForPlayer(player, 2, 0));
                    break;
                case PR:
                    players.forEach(player -> createPlaysForPlayer(player, 1, 1));
                    break;
                case RR:
                    players.forEach(player -> createPlaysForPlayer(player, 0, 2));
                    break;
                case PPR:
                    players.forEach(player -> createPlaysForPlayer(player, 2, 1));
                    break;
                case PRR:
                    players.forEach(player -> createPlaysForPlayer(player, 1, 2));
                    break;
                case PPP:
                    players.forEach(player -> createPlaysForPlayer(player, 3, 0));
                    break;
                case RRR:
                    players.forEach(player -> createPlaysForPlayer(player, 0, 3));
                    break;
                case START:
                case END:
                    // Create no plays
                    break;
                default:
                    _LOGGER.warn("Didn't match round %s; not creating any plays", round);
                    break;
            }
        }
    }
}
