package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

public class TriggerHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTrigger() && activity.getDelta() instanceof Card && activity.getChildren().size() > 0;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card card = context.getBefore(activity);
        Player cardController = card.getController().equals(context.getFriendlyPlayer().getController()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();
        result.addTrigger(cardController, card);
        return true;
    }
}
