package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;

public class DamageHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getPredamage() != null && !FALSE_OR_ZERO.equals(context.getAfter(activity).getPredamage());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        Card attacker = (Card) context.getEntityById(context.getGameEntity().getProposedAttacker());
        Card defender = (Card) context.getEntityById(context.getGameEntity().getProposedDefender());
        if (attacker == null && defender == null) {
            if (activity.getParent().getDelta() instanceof Card && activity.getParent().getTarget() != null && activity.getParent().getTarget() instanceof Card) {
                attacker = (Card) activity.getParent().getDelta();
                defender = (Card) activity.getParent().getTarget();
            } else {
                attacker = (Card) activity.getParent().getDelta();
                defender = before;
            }
        }

        int damage = Integer.parseInt(after.getPredamage());
        if (before == attacker) {
            if (defender == null) {
                result.addDamage(attacker, before, damage);
                System.out.println(context.getSide(attacker) + " " + attacker.getName() + " has done " + damage + " damage to " + context.getSide(before) + before.getName());
                return true;
            } else {
                result.addDamage(defender, before, damage);
                System.out.println(context.getSide(defender) + " " + defender.getName() + " has done " + damage + " damage to " + context.getSide(before) + " " + before.getName());
                return true;
            }
        } else if (before == defender) {
            result.addDamage(attacker, before, damage);
            System.out.println((attacker != null ? context.getSide(attacker) + " " + attacker.getName() : "") + " has done " + damage + " damage to " + context.getSide(before) + " " + before.getName());
            return true;
        } else {
            result.addDamage(attacker, before, damage);
            System.out.println((attacker != null ? context.getSide(attacker) + " " + attacker.getName() : "") + " has done " + damage + " damage to " + context.getSide(before) + " " + before.getName());
            return true;
        }
    }
}
