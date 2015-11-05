package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.play.MatchResult;

public class HeroPowerHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isPower() && activity.getDelta() instanceof Card;
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card card = context.getBefore(activity);
        Player player = context.getPlayerForCard(card);

        if (Card.Type.HERO_POWER.eq(card.getCardtype())) {
            System.out.println(player.getName() + " has activated their hero power");
            result.addHeroPowerUsed(player, card);
            return true;
        }
        return false;
    }
}
