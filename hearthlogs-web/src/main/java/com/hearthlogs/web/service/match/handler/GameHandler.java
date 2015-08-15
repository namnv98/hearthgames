package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Game;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class GameHandler extends ActivityHandler {

    protected void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Game before, Game after) {

        if (before.getStep() == null && "BEGIN_MULLIGAN".equals(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            context.getCards().stream().filter(c -> "HAND".equals(c.getZone())).forEach(c -> {
                if (getPlayer(context, c) == context.getFriendlyPlayer()) {
                    match.addFriendlyStartingCard(c.getCardid());
                } else {
                    match.addOpposingStartingCard(c.getCardid());
                }
                System.out.println(getPlayer(context, c).getName() + " has drawn " + getName(c.getCardid()));
            });


            System.out.println();
            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
            System.out.println();
        }
        if ("BEGIN_MULLIGAN".equals(before.getStep()) && "MAIN_READY".equals(after.getStep())) {
            System.out.println();
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
            System.out.println();
        }
        if ("MAIN_READY".equals(after.getStep())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
            printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
            printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);


            match.setTurns(Integer.valueOf(before.getTurn()));
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + before.getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        if ("COMPLETE".equals(after.getState())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
            printHeroHealth(context.getFriendlyPlayer(), friendlyHeroCard);
            printHeroHealth(context.getOpposingPlayer(), opposingHeroCard);

            System.out.println("--------------------------  Game Over  ----------------------------------------");
        }

    }

}
