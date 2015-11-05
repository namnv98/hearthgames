package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.play.MatchResult;

public class TriggerHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTrigger() && activity.getDelta() instanceof Card && activity.getChildren().size() > 0;
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        result.addTrigger(context.getBefore(activity));
        return true;
    }
}
