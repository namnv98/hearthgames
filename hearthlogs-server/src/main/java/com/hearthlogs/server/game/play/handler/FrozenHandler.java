package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;

public class FrozenHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getFrozen() != null;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        boolean frozen = TRUE_OR_ONE.equals(after.getFrozen());
        result.addFrozen(before, frozen);

        System.out.println(context.getSide(before) + " " + before.getName() + " has been frozen");

        return true;
    }
}
