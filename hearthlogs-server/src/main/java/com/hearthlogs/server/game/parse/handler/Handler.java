package com.hearthlogs.server.game.parse.handler;

import com.hearthlogs.server.game.log.domain.LogLineData;
import com.hearthlogs.server.game.parse.GameContext;

public interface Handler {

    boolean supports(GameContext context, String line);

    boolean handle(GameContext context, LogLineData line);
}
