package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.play.GameResult;

public class CardCreationHandler implements Handler {
    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isNewCard() && context.getGameEntity().isGameRunning();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
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