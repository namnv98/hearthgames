package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.*;
import com.hearthlogs.server.match.play.MatchResult;

public class CardDrawnHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return (activity.isShowEntity() || activity.isTagChange()) && (activity.getDelta() instanceof Card) && context.getAfter(activity).getZone() != null && Zone.DECK.eq(context.getBefore(activity).getZone()) && Zone.HAND.eq(context.getAfter(activity).getZone());
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Player player = context.getPlayerForCard(before);

        if (context.getGame().isMulliganOver()) {
            result.addCardDrawn(player, before, activity.getParent().getDelta());
            if (activity.getParent() != null && (activity.getParent().isTrigger() || activity.getParent().isPower())) {
                Entity entity = activity.getParent().getDelta();
                if (entity instanceof Card) {
                    System.out.println(player.getName() + " has drawn " + before.getName() + ", id="+before.getEntityId());
                } else if (entity instanceof Player) {
                    System.out.println(player.getName() + " has drawn " +  before.getName() + ", id="+before.getEntityId());
                }
                return true;
            }
        } else {
            if (player == context.getFriendlyPlayer()) {
                if (context.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                    result.addFriendlyStartingCard(before);
                    System.out.println(player.getName() + " has drawn " +  before.getName() + ", id="+before.getEntityId());
                    return true;
                }
            } else {
                if (context.getStartingCardIds().contains(before.getEntityId())) {
                    result.addOpposingStartingCard(before);
                    System.out.println(player.getName() + " has drawn " +  before.getName() + ", id="+before.getEntityId());
                    return true;
                }
            }
        }
        return false;
    }
}
