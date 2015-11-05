package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Zone;
import com.hearthlogs.server.match.play.MatchResult;

public class HealthChangeHandler implements Handler {
    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getHealth() != null && Zone.PLAY.eq(context.getBefore(activity).getZone());
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
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
