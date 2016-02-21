package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.GameContext;

public interface Handler {

    boolean supports(GameContext gameContext);

    boolean handle(GameContext gameContext);

}
