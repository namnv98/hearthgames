package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;

public interface Analyzer<T> {

    T analyze(GameResult result, GameContext context);

}
