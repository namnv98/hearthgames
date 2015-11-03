package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.domain.*;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.ActionFactory;
import com.hearthlogs.server.match.play.domain.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameHandler extends ActivityHandler {

    @Autowired
    public GameHandler(ActionFactory actionFactory) {
        super(actionFactory);
    }

    protected void handleNewGame(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        result.addTurn(actionFactory.createTurn(1));
    }

    protected void handleGameTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Game before, Game after) {

        if (before.getStep() == null && Game.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            parsedMatch.getCards().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> parsedMatch.getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                Player player = c.getController().equals(parsedMatch.getFriendlyPlayer().getController()) ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer();
                if (player == parsedMatch.getFriendlyPlayer()) {
                    result.addFriendlyStartingCard(c);
                } else {
                    result.addOpposingStartingCard(c);
                }
                System.out.println(player.getName() + " has drawn2 " + c.getName() + " (id=" + c.getEntityId()+ ")");
            });


            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
        }
        if (Game.Step.BEGIN_MULLIGAN.eq(before.getStep()) && Game.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + parsedMatch.getGame().getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        if (Game.Step.MAIN_NEXT.eq(after.getStep())) {

            Set<Card> friendlyCardsInPlay = new HashSet<>();
            Set<Card> opposingCardsInPlay = new HashSet<>();

            for (Turn turn: result.getTurns()) {
                friendlyCardsInPlay.addAll(turn.getFriendlyCardsPutInPlay().stream().collect(Collectors.toList()));
                turn.getFriendlyCardsRemovedFromPlay().forEach(friendlyCardsInPlay::remove);
                opposingCardsInPlay.addAll(turn.getOpposingCardsPutInPlay().stream().collect(Collectors.toList()));
                turn.getOpposingCardsRemovedFromPlay().forEach(opposingCardsInPlay::remove);
            }

            result.getCurrentTurn().setFriendlyCardsInPlay(friendlyCardsInPlay);
            result.getCurrentTurn().setOpposingCardsInPlay(opposingCardsInPlay);


        }

        if (Game.Step.MAIN_READY.eq(after.getStep())) {
            result.getCurrentTurn().setStartDateTime(activity.getDateTime());
        } else if (Game.Step.MAIN_NEXT.eq(after.getStep()) || Game.Step.FINAL_GAMEOVER.eq(after.getStep())) {
            result.getCurrentTurn().setEndDateTime(activity.getDateTime());
        }

        if (Game.Step.MAIN_START.eq(after.getStep())) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            if (result.getCurrentTurn().getManaGained() == 0) {
                result.getCurrentTurn().addManaGained(10);
            }

        }

        if (after.getTurn() != null && parsedMatch.getGame().isGameRunning()) {

            int turnNumber = Integer.parseInt(after.getTurn());
            Turn turn = actionFactory.createTurn(turnNumber);
            result.addTurn(turn);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + turnNumber + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        if (Game.State.COMPLETE.eq(after.getState())) {
            Card friendlyHeroCard = (Card) parsedMatch.getEntityById(parsedMatch.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) parsedMatch.getEntityById(parsedMatch.getOpposingPlayer().getHeroEntity());

            CardDetails cardDetails = friendlyHeroCard.getCardDetails();
            result.setWinnerClass(cardDetails.getPlayerClass());

            cardDetails = opposingHeroCard.getCardDetails();
            result.setLoserClass(cardDetails.getPlayerClass());


            System.out.println("--------------------------  Game Over  ----------------------------------------");
        }

    }

}
