package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.GameResult;

public class NewGameHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isNewGame();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        result.setTurnNumber(1);
        result.addTurn();
        context.getCards().stream().filter(card -> Zone.HAND.eq(card.getZone()) && context.getFriendlyPlayer().getController().equals(card.getController())).forEach(card -> {
            result.getCurrentTurn().getFriendlyCardsInHand().add(card);
        });
        context.getCards().stream().filter(card -> Zone.HAND.eq(card.getZone()) && context.getOpposingPlayer().getController().equals(card.getController())).forEach(card -> {
            result.getCurrentTurn().getOpposingCardsInHand().add(card);
        });
        return true;
    }
}
