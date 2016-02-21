package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

public class CardCreationHandler implements Handler {
    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isNewCard() &&
               gameContext.getGameState().getGameEntity().isGameRunning();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        if (gameContext.getActivity().getParent() == null || !gameContext.getActivity().getParent().isJoust()) { // Cards created during the joust are not part of your deck but do represent a copy of card in your deck, they are temporary.
            Card created = gameContext.getAfter();;
            if (created.getController() != null && created.getCreator() != null) {
                Player beneficiary = gameContext.getGameState().getPlayerForCard(created);
                Card creator = gameContext.getGameState().getEntityById(created.getCreator());
                Player creatorController = null;
                Player createdController = gameContext.getGameState().getPlayer(created);
                if (creator != null) {
                    creatorController = gameContext.getGameState().getPlayer(creator);
                }

                if (!created.isEnchantment()) {
                    gameContext.addCardCreation(creatorController, createdController, beneficiary, creator, created);
                }
                return true;
            }
        }
        return false;
    }
}