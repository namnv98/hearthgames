package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameContext;

public interface Handler {

    boolean supports(GameContext context, String line);

    boolean handle(GameContext context, LogLineData line);
}
