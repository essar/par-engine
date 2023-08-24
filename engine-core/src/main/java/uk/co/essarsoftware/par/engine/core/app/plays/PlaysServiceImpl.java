package uk.co.essarsoftware.par.engine.core.app.plays;

import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.co.essarsoftware.par.cards.Card;
import uk.co.essarsoftware.par.cards.Play;
import uk.co.essarsoftware.par.engine.core.app.CardEncoder;
import uk.co.essarsoftware.par.engine.core.app.InvalidPlayException;
import uk.co.essarsoftware.par.engine.core.app.actions.ActionsService;
import uk.co.essarsoftware.par.engine.players.Player;
import uk.co.essarsoftware.par.engine.plays.PlaySet;
import uk.co.essarsoftware.par.engine.plays.PlaysService;


@Service
public class PlaysServiceImpl implements PlaysService
{

    private static final Logger _LOG = LoggerFactory.getLogger(ActionsService.class);

    private final PlaySet plays;

    public PlaysServiceImpl(PlaySet plays) {

        this.plays = plays;

    }

    private void addCardToPlay(Play play, Card card) {

        try {
        
            _LOG.debug("Adding {} to {} {}", CardEncoder.asShortString(card), play.getClass().getSimpleName(), CardEncoder.asShortString(play.getCards()));
            play.addCard(card);
        
        } catch (IllegalArgumentException ie) {

            // Log and continue
            _LOG.debug("Card {} cannot be added to {} {}", CardEncoder.asShortString(card), play.getClass().getSimpleName(), CardEncoder.asShortString(play.getCards()));

        }
    }

    Play buildPlay(final Play play, final Card[] cards) {

        // Check if play is empty and refuse otherwise
        if (play.size() != 0) {

            _LOG.warn("Not adding cards to existing play");
            return play;

        }

        // For each card provided, sort them by the order specified by the play and try to add to the play and check the outcome
        Arrays.stream(cards)
            .sorted(play)
            .forEach(c -> addCardToPlay(play, c));

        // Clean up if we've not completed a valid play
        if (! isValidPlay(play)) {

            play.reset();

        }

        return play;

    }

    boolean isValidPlay(Play play) {

        return Objects.nonNull(play) && (play.isPrial() || play.isRun());

    }

    public Play buildPlayForPlayer(final Player player, final Card[] cards) {

        _LOG.debug("{} trying to build a play from {}", player.getPlayerName(), CardEncoder.asShortString(cards));

        // Check we have three cards
        if (cards.length != 3) {

            throw new InvalidPlayException("Play must contain three cards");

        }

        // Try and find an available play
        Play play = Arrays.stream(plays.getAvailablePlays(player))
            .map(p -> buildPlay(p, cards))
            .filter(this::isValidPlay)
            .findAny()
            .orElseThrow(() -> new InvalidPlayException(String.format("Cards %s cannot be used to build a valid play", CardEncoder.asShortString(cards))));

        return play;

    }

    public boolean hasAvailablePlaysRemaining(Player player) {

        return plays.getAvailablePlays(player).length > 0;

    }
}
