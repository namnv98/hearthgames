package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;

public class TriggerHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTrigger() && activity.getDelta() instanceof Card && activity.getChildren().size() > 0;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        result.addTrigger(context.getBefore(activity));
        return true;
    }
}
