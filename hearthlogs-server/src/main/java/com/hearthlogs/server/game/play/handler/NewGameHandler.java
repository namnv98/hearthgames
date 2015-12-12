package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isNewGame();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        result.setTurnNumber(0);
        result.addTurn();
        return true;
    }
}
