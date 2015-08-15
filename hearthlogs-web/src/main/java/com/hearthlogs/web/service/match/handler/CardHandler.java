package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class CardHandler extends ActivityHandler {

    public void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Player player, Card before, Card after) {

        if ("HAND".equals(before.getZone()) && "DECK".equals(after.getZone())) {

            if (player == context.getFriendlyPlayer()) {
                match.mulliganFriendlyCard(before.getCardid());
            } else {
                match.mulliganOpposingCard(before.getCardid());
            }

            System.out.println(player.getName() + " has mulliganed " + getName(before.getCardid()));
        }
        if ("DECK".equals(before.getZone()) && "HAND".equals(after.getZone())) {

            if (player == context.getFriendlyPlayer()) {
                if (context.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                    match.addFriendlyCard(before.getCardid());
                }
            } else {
                if (context.getStartingCardIds().contains(before.getEntityId())) {
                    match.addOpposingCard(before.getCardid());
                }
            }
            System.out.println(player.getName() + " has drawn " + getName(before.getCardid()));
        }
        if ("HAND".equals(before.getZone()) && "PLAY".equals(after.getZone()) && "MINION".equals(before.getCardtype())) {
            System.out.println(player.getName() + " has played " + getName(before.getCardid()));
        }
        if ("PLAY".equals(before.getZone()) && "GRAVEYARD".equals(after.getZone()) && "MINION".equals(before.getCardtype())) {
            System.out.println(getName(before.getCardid()) + " has died.");
        }
    }
}