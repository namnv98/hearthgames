package com.hearthlogs.server.match.handler;

import com.hearthlogs.server.match.domain.*;
import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import com.hearthlogs.server.match.result.Turn;
import org.springframework.stereotype.Component;

@Component
public class GameHandler extends ActivityHandler {

    protected void handleNewGame(MatchResult result, MatchContext context, Activity activity) {
        result.addTurn(new Turn(1));
    }

    protected void handleTagChange(MatchResult result, MatchContext context, Activity activity, Game before, Game after) {

        if (before.getStep() == null && Game.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            context.getCards().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> context.getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                if (getPlayer(context, c) == context.getFriendlyPlayer()) {
                    result.addFriendlyStartingCard(c);
                } else {
                    result.addOpposingStartingCard(c);
                }
                System.out.println(getPlayer(context, c).getName() + " has drawn2 " + getName(c) + " (id=" + c.getEntityId()+ ")");
            });


            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
        }
        if (Game.Step.BEGIN_MULLIGAN.eq(before.getStep()) && Game.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + context.getGame().getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        if (Game.Step.MAIN_READY.eq(before.getStep())) {




        }

        if (Game.Step.MAIN_START.eq(after.getStep())) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            if (result.getCurrentTurn().getManaGained() == 0) {
                result.getCurrentTurn().addManaGained(10);
            }

        }

        if (after.getTurn() != null && context.getGame().isGameRunning()) {

            int turnNumber = Integer.parseInt(after.getTurn());
            Turn turn = new Turn(turnNumber);
            result.addTurn(turn);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + turnNumber + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

    }

}
