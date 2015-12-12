package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.play.GameResult;

public class ArmorChangeHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getArmor() != null;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int armor = Integer.parseInt(after.getArmor());
        Player player = context.getPlayer(before);
        result.addArmorChange(player, before, armor);

        return true;
    }
}
