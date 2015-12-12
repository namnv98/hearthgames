package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;

public class CardDrawnHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return (activity.isShowEntity() || activity.isTagChange()) && (activity.getDelta() instanceof Card) && context.getAfter(activity).getZone() != null && Zone.DECK.eq(context.getBefore(activity).getZone()) && Zone.HAND.eq(context.getAfter(activity).getZone());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Player player = context.getPlayerForCard(before);

        if (context.getGameEntity().isMulliganOver()) {
            if (context.getStartingCardIds().contains(before.getEntityId())) {
                if (player == context.getFriendlyPlayer()) {
                    result.addFriendlyDeckCard(before);
                } else {
                    result.addOpposingDeckCard(before);
                }
            }
            result.addCardDrawn(player, before, activity.getParent().getDelta());
        } else {
            if (player == context.getFriendlyPlayer()) {
                if (context.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                    result.addFriendlyStartingCard(before);
                    result.addLoggingAction(player.getName() + " has drawn " +  before.getName());
                    return true;
                }
            } else {
                if (context.getStartingCardIds().contains(before.getEntityId())) {
                    result.addOpposingStartingCard(before);
                    result.addLoggingAction(player.getName() + " has drawn " +  before.getName());
                    return true;
                }
            }
        }
        return false;
    }
}
