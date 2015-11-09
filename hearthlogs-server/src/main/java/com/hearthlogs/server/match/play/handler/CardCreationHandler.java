package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.play.MatchResult;

public class CardCreationHandler implements Handler {
    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return activity.isNewCard() && context.getGame().isGameRunning();
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        if (activity.getParent() == null || !activity.getParent().isJoust()) { // Cards created during the joust are not part of your deck but do represent a copy of card in your deck, they are temporary.
            Card created = context.getAfter(activity);;
            if (created.getController() != null && created.getCreator() != null) {
                Player beneficiary = context.getPlayerForCard(created);
                Card creator = (Card) context.getEntityById(created.getCreator());
                result.addCardCreation(beneficiary, creator, created);
                System.out.println(context.getSide(creator) + " " + creator.getName() + " has created : " + context.getSide(created) + " " +  created.getName());

                return true;
            }
        }
        return false;
    }
}