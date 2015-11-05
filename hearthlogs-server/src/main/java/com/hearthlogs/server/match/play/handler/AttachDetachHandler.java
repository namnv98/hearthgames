package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Entity;
import com.hearthlogs.server.match.play.MatchResult;

public class AttachDetachHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isTagChange() && (activity.getDelta() instanceof Card) && context.getAfter(activity).getAttached() != null;
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        if (FALSE_OR_ZERO.equals(after.getAttached())) {
            Entity entity = context.getEntityById(before.getAttached());
            if (entity instanceof Card) {
                Card detachFrom = (Card) entity;
                result.addDetached(before, detachFrom);
//                System.out.println("Detached card : " + before.getName() + " from " + detachFrom.getName());
                return true;
            }
        } else {
            Entity entity = context.getEntityById(after.getAttached());
            if (entity instanceof Card) {
                Card attachTo = (Card) entity;
                result.addAttached(before, attachTo);
//                System.out.println("Attach card : " + before.getName() + " to " + attachTo.getName() + " : " + before.getText());
                return true;

            }
        }
        return false;
    }
}
