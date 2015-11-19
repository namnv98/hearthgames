package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;

public interface Analyzer<T> {

    T analyze(GameResult result, GameContext context);

}
