package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.PlayContext;

public interface Handler {

    String TRUE_OR_ONE = "1";
    String FALSE_OR_ZERO = "0";

    boolean supports(PlayContext playContext);

    boolean handle(PlayContext playContext);

}
