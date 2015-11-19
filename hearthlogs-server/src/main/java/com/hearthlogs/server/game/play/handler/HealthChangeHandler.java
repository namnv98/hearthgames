package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.GameResult;

public class HealthChangeHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getHealth() != null && Zone.PLAY.eq(context.getBefore(activity).getZone());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int newHealth = Integer.parseInt(after.getHealth());
        int currentHealth = Integer.parseInt(before.getHealth() == null ? before.getCardDetailsHealth() : before.getHealth());

        int diffHealth = newHealth - currentHealth;
        result.addHealthChange(before, diffHealth);

        System.out.println(context.getSide(before) + " " + before.getName() + " health is now : " + newHealth);
        return true;
    }
}
