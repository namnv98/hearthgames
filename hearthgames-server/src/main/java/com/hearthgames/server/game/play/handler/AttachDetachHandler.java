package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Entity;
import com.hearthgames.server.game.play.GameResult;

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
                return true;
            }
        } else {
            Entity entity = context.getEntityById(after.getAttached());
            if (entity instanceof Card) {
                Card attachTo = (Card) entity;
                result.addAttached(before, attachTo);
                return true;
            }
        }
        return false;
    }
}
