package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Entity;
import com.hearthlogs.server.game.play.GameResult;

public class AttachDetachHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getAttached() != null;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        if (FALSE_OR_ZERO.equals(after.getAttached())) {
            Entity entity = context.getEntityById(before.getAttached());
            if (entity instanceof Card) {
                Card detachFrom = (Card) entity;
                result.addDetached(before, detachFrom);
//                System.out.println("Detached card : " + before.getName() + " from " + detachFrom.getName());
                return true;
            }
        } else {
            Entity entity = context.getEntityById(after.getAttached());
            if (entity instanceof Card) {
                Card attachTo = (Card) entity;
                result.addAttached(before, attachTo);
//                System.out.println("Attach card : " + before.getName() + " to " + attachTo.getName() + " : " + before.getText());
                return true;

            }
        }
        return false;
    }
}
