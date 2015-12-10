package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.GameResult;

public class AttackChangeHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getAtk() != null && Zone.PLAY.eq(context.getBefore(activity).getZone());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int newAttack = Integer.parseInt(after.getAtk());
        int currentAttack = Integer.parseInt(before.getAtk() == null ? before.getCardDetailsAttack() : before.getAtk());

        int diffAttack = newAttack - currentAttack;
        String side = context.getSide(before);
        result.addAttackChange(side, before, diffAttack, newAttack);

        result.addActionLog(side + " " + before.getName() + " attack is now : " + newAttack);

        return true;
    }
}
