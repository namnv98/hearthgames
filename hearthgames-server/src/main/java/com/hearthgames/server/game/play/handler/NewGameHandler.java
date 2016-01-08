package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.PlayContext;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isNewGame();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        playContext.getResult().setTurnNumber(0);
        playContext.getResult().addTurn();
        return true;
    }
}
