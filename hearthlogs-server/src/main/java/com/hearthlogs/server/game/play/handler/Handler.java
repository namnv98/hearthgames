package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.play.GameResult;

public interface Handler {

    String TRUE_OR_ONE = "1";
    String FALSE_OR_ZERO = "0";

    boolean supports(GameResult result, GameContext context, Activity activity);

    boolean handle(GameResult result, GameContext context, Activity activity);

}
