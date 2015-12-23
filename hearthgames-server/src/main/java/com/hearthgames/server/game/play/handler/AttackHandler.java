package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

public class AttackHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity != null && activity.isAttack();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {

        Card attacker = (Card) context.getEntityById(activity.getDelta().getEntityId());
        Card defender = (Card) context.getEntityById(activity.getTarget().getEntityId());

        Player attackerController = attacker.getController().equals(context.getFriendlyPlayer().getController()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();
        Player defenderController = defender.getController().equals(context.getFriendlyPlayer().getController()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();

        result.addAttack(attacker, defender, attackerController, defenderController);

        return false;
    }
}
