package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.play.MatchResult;

public class HeroHealthChangeHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card)
                && context.getAfter(activity).getDamage() != null
                && !FALSE_OR_ZERO.equals(context.getAfter(activity).getDamage())
                && Card.Type.HERO.eq(context.getBefore(activity).getCardtype());
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int health = Integer.parseInt(before.getCardDetailsHealth());
        int damage = Integer.parseInt(after.getDamage());
        int newHealth = health - damage;

        result.addHeroHealthChange(before, newHealth);
        System.out.println(context.getSide(before) + " " + before.getName() + " hero health is now : " + (health - damage));
        return true;
    }
}
