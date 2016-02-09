package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.PlayContext;
import com.hearthgames.server.game.play.domain.Turn;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isNewGame();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        playContext.getResult().setTurnNumber(0);
        playContext.getResult().addTurn();
        Turn turn = playContext.getResult().getCurrentTurn();
        turn.setWhoseTurn(TRUE_OR_ONE.equals(playContext.getContext().getFriendlyPlayer().getFirstPlayer()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer());
        return true;
    }
}
