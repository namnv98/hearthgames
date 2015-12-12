package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

public class MulliganCardHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return (activity.isHideEntity() || activity.isTagChange()) && activity.getDelta() instanceof Card && context.getAfter(activity).getZone() != null && Zone.HAND.eq(context.getBefore(activity).getZone()) && Zone.DECK.eq(context.getAfter(activity).getZone()) && Player.DEALING.equals(context.getPlayerForCard(context.getBefore(activity)).getMulliganState());
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Player player = context.getPlayerForCard(before);

        if (context.isFriendly(player)) {
            result.mulliganFriendlyCard(before);
        } else {
            result.mulliganOpposingCard(before);
        }
        result.addLoggingAction(player.getName() + " has mulliganed " + before.getName());
        return true;
    }
}
