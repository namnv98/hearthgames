package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Zone;
import com.hearthlogs.server.match.play.MatchResult;

public class ZonePositionChangeHandler implements Handler {
    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof Card && context.getAfter(activity).getZonePosition() != null && context.getGame().isMulliganOver();
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        result.addZonePositionChange(before, Zone.valueOf(before.getZone()), Integer.parseInt(after.getZonePosition()));
//        System.out.println(before.getCardDetails() == null ? "unknown card" : before.getName() + " has moved to " + before.getZone() + ", position " + after.getZonePosition());
        return true;
    }
}
