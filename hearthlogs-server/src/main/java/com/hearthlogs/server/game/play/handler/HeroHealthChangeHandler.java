package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;

public class HeroHealthChangeHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card)
                && context.getAfter(activity).getDamage() != null
                && !FALSE_OR_ZERO.equals(context.getAfter(activity).getDamage())
                && Card.Type.HERO.eq(context.getBefore(activity).getCardtype());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int health = Integer.parseInt(before.getCardDetailsHealth());
        int damage = Integer.parseInt(after.getDamage());
        int newHealth = health - damage;

        String side = context.getSide(before);
        result.addHeroHealthChange(side, before, newHealth);
        System.out.println(side + " " + before.getName() + " hero health is now : " + (health - damage));
        return true;
    }
}
