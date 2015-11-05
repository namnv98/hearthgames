package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.play.MatchResult;

public class ArmorChangeHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getArmor() != null;
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int armor = Integer.parseInt(after.getArmor());
        result.addArmorChange(before, armor);
        System.out.println(context.getSide(before) + " " + before.getName() + " armor is now : " + armor);

        return true;
    }
}
