package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.play.GameContext;
import com.hearthgames.server.game.play.domain.Turn;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isNewGame();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        gameContext.getResult().setTurnNumber(0);
        gameContext.getResult().addTurn();
        Turn turn = gameContext.getResult().getCurrentTurn();
        turn.setWhoseTurn(TRUE_OR_ONE.equals(gameContext.getGameState().getFriendlyPlayer().getFirstPlayer()) ? gameContext.getGameState().getFriendlyPlayer() : gameContext.getGameState().getOpposingPlayer());
        return true;
    }
}
