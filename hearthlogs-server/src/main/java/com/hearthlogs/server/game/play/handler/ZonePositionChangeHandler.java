package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.GameResult;

public class ZonePositionChangeHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof Card && context.getAfter(activity).getZonePosition() != null && context.getGameEntity().isMulliganOver();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        result.addZonePositionChange(before, Zone.valueOf(before.getZone()), Integer.parseInt(after.getZonePosition()));
//        System.out.println(before.getCardDetails() == null ? "unknown card" : before.getName() + " has moved to " + before.getZone() + ", position " + after.getZonePosition());
        return true;
    }
}
