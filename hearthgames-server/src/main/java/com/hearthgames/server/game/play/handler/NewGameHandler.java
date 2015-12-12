package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.play.GameResult;

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
