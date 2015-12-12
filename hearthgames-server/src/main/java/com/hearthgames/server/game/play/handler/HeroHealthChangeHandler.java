package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

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

        Player player = context.getPlayer(before);
        result.addHeroHealthChange(player, before, newHealth);
        return true;
    }
}
