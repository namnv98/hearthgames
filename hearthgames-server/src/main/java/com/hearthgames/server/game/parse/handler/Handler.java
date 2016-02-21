package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;

public interface Handler {

    boolean supports(GameState gameState, String line);

    boolean handle(GameState gameState, LogLineData line);
}
