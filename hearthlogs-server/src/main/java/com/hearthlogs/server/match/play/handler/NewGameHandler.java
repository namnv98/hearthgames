package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.play.MatchResult;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isNewGame();
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        result.setTurnNumber(1);
        result.addTurn();
        return true;
    }
}
