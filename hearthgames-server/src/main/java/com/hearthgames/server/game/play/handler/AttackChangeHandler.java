package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameResult;

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
        Player player = context.getPlayer(before);
        result.addAttackChange(player, before, diffAttack, newAttack);

        return true;
    }
}
