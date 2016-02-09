package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.PlayContext;

public interface Handler {

    boolean supports(PlayContext playContext);

    boolean handle(PlayContext playContext);

}
