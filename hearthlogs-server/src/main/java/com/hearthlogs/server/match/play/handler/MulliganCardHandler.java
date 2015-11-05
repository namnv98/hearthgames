package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.domain.Zone;
import com.hearthlogs.server.match.play.MatchResult;

public class MulliganCardHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return (activity.isHideEntity() || activity.isTagChange()) && activity.getDelta() instanceof Card && context.getAfter(activity).getZone() != null && Zone.HAND.eq(context.getBefore(activity).getZone()) && Zone.DECK.eq(context.getAfter(activity).getZone()) && Player.DEALING.equals(context.getPlayerForCard(context.getBefore(activity)).getMulliganState());
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Player player = context.getPlayerForCard(before);

        if (context.isFriendly(player)) {
            result.mulliganFriendlyCard(before);
        } else {
            result.mulliganOpposingCard(before);
        }
        System.out.println(player.getName() + " has mulliganed " + before.getName());
        return true;
    }
}
