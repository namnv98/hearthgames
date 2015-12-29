package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameResult;

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
        int currentHealth = before.getHealth() == null ? before.getCardDetailsHealth() : Integer.parseInt(before.getHealth());

        int diffHealth = newHealth - currentHealth;
        Player player = context.getPlayer(before);
        result.addHealthChange(player, before, diffHealth, newHealth);

        return true;
    }
}
