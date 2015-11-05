package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Zone;
import com.hearthlogs.server.match.play.MatchResult;

public class AttackChangeHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getAtk() != null && Zone.PLAY.eq(context.getBefore(activity).getZone());
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        int newAttack = Integer.parseInt(after.getAtk());
        int currentAttack = Integer.parseInt(before.getAtk() == null ? before.getCardDetailsAttack() : before.getAtk());

        int diffAttack = newAttack - currentAttack;
        result.addAttackChange(before, diffAttack);
        System.out.println(context.getSide(before) + " " + before.getName() + " attack is now : " + newAttack);

        return true;
    }
}
