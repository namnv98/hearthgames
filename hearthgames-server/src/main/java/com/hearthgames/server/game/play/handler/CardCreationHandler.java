package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

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
                Player creatorController = context.getPlayer(creator);
                Player createdController = context.getPlayer(created);

                if (!created.isEnchantment()) {
                    result.addCardCreation(creatorController, createdController, beneficiary, creator, created);
                }
                return true;
            }
        }
        return false;
    }
}