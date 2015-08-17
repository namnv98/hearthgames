package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Game;
import com.hearthlogs.web.domain.Zone;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class GameHandler extends ActivityHandler {

    protected void handleTagChange(MatchContext context, Activity activity, Game before, Game after) {

        if (before.getStep() == null && Game.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            context.getCards().stream().filter(c -> Zone.HAND.eq(c.getZone())).forEach(c -> {
                if (getPlayer(context, c) == context.getFriendlyPlayer()) {
                    context.addFriendlyStartingCard(c);
                } else {
                    context.addOpposingStartingCard(c);
                }
                System.out.println(getPlayer(context, c).getName() + " has drawn " + getName(c.getCardid()));
            });


            System.out.println();
            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
            System.out.println();
        }
        if (Game.Step.BEGIN_MULLIGAN.eq(before.getStep()) && Game.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println();
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
            System.out.println();
        }
        if (Game.Step.MAIN_READY.eq(after.getStep())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
            printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
            printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);


            context.setTurns(before.getTurn());
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + before.getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if (Game.State.COMPLETE.eq(after.getState())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
            printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
            printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);

            System.out.println("--------------------------  Game Over  ----------------------------------------");
        }

    }

}
