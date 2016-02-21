package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

public class TriggerHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTrigger() &&
               gameContext.getActivity().isCard() &&
               gameContext.getActivity().getChildren().isEmpty();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card card = gameContext.getBefore();
        Player cardController = card.getController().equals(gameContext.getGameState().getFriendlyPlayer().getController()) ? gameContext.getGameState().getFriendlyPlayer() : gameContext.getGameState().getOpposingPlayer();
        gameContext.addTrigger(cardController, card);
        return true;
    }
}
