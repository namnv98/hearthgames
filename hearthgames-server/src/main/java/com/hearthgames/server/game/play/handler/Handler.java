package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.play.GameResult;

public interface Handler {

    String TRUE_OR_ONE = "1";
    String FALSE_OR_ZERO = "0";

    boolean supports(GameResult result, GameContext context, Activity activity);

    boolean handle(GameResult result, GameContext context, Activity activity);

}
