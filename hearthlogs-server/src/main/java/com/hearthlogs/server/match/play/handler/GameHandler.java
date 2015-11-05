package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.domain.*;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof Game;
    }

    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Game before = context.getGame();
        Game after = (Game) activity.getDelta();

        if (before.getStep() == null && Game.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            context.getCards().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> context.getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                Player player = c.getController().equals(context.getFriendlyPlayer().getController()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();
                if (player == context.getFriendlyPlayer()) {
                    result.addFriendlyStartingCard(c);
                } else {
                    result.addOpposingStartingCard(c);
                }
                System.out.println(player.getName() + " has drawn " + c.getName() + " (id=" + c.getEntityId()+ ")");
            });


            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
        }
        if (Game.Step.BEGIN_MULLIGAN.eq(before.getStep()) && Game.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + context.getGame().getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + result.getTurnNumber() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            if (result.getTurnNumber() > 1) {
                result.addTurn();
            }
            result.getCurrentTurn().setStartDateTime(activity.getDateTime());
        } else if (Game.Step.MAIN_NEXT.eq(after.getStep()) || Game.Step.FINAL_GAMEOVER.eq(after.getStep())) {
            result.getCurrentTurn().setEndDateTime(activity.getDateTime());
        }

        if (Game.Step.MAIN_START.eq(after.getStep())) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            if (result.getCurrentTurn().getManaGained() == 0) {
                result.addManaGained(10);
            }
        }

        if (after.getTurn() != null && context.getGame().isGameRunning()) {
            int turnNumber = Integer.parseInt(after.getTurn());
            result.setTurnNumber(turnNumber);
            result.getCurrentTurn().setWhoseTurn(result.getCurrentPlayer());
        }

        if (Game.State.COMPLETE.eq(after.getState())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());

            CardDetails cardDetails = friendlyHeroCard.getCardDetails();
            result.setWinnerClass(cardDetails.getPlayerClass());

            cardDetails = opposingHeroCard.getCardDetails();
            result.setLoserClass(cardDetails.getPlayerClass());


            System.out.println("--------------------------  Game Over  ----------------------------------------");
        }

        return true;
    }

}
