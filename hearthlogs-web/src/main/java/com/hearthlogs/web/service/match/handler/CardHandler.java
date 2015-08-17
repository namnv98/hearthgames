package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.domain.Zone;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class CardHandler extends ActivityHandler {

    public void handleTagChange(MatchContext context, Activity activity, Player player, Card before, Card after) {

        if (Zone.HAND.eq(before.getZone()) && Zone.DECK.eq(after.getZone())) {

            if ("DEALING".equals(player.getMulliganState())) {
                if (player == context.getFriendlyPlayer()) {
                    context.mulliganFriendlyCard(before);
                } else {
                    context.mulliganOpposingCard(before);
                }
            }

            System.out.println(player.getName() + " has mulliganed " + getName(before.getCardid()));
        }
        if (Zone.DECK.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {

            if ("DEALING".equals(player.getMulliganState())) {
                if (player == context.getFriendlyPlayer()) {
                    context.addFriendlyStartingCard(before);
                } else {
                    context.addOpposingStartingCard(before);
                }
            } else {
                if (player == context.getFriendlyPlayer()) {
                    if (context.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                        context.addFriendlyCard(before);
                    } else {
                        context.addFriendlyCreatedCard(before);
                    }
                } else {
                    if (context.getStartingCardIds().contains(before.getEntityId())) {
                        context.addOpposingCard(before);
                    } else {
                        context.addOpposingCreatedCard(before);
                    }
                }
            }
            System.out.println(player.getName() + " has drawn " + getName(before.getCardid()));
        } else if (!Zone.HAND.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {
            System.out.println(player.getName() + " has " + getName(before.getCardid()) + " from " + before.getZone());
        }

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            System.out.println(player.getName() + " has played " + getName(before.getCardid()));
        }

        if (Zone.PLAY.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            System.out.println(getName(before.getCardid()) + " has died.");
        }
    }
}