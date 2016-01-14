package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class CardCreationHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isNewCard() && playContext.getContext().getGameEntity().isGameRunning();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        if (playContext.getActivity().getParent() == null || !playContext.getActivity().getParent().isJoust()) { // Cards created during the joust are not part of your deck but do represent a copy of card in your deck, they are temporary.
            Card created = playContext.getContext().getAfter(playContext.getActivity());;
            if (created.getController() != null && created.getCreator() != null) {
                Player beneficiary = playContext.getContext().getPlayerForCard(created);
                Card creator = playContext.getContext().getCardByEntityId(created.getCreator());
                Player creatorController = null;
                Player createdController = playContext.getContext().getPlayer(created);
                if (creator != null) {
                    creatorController = playContext.getContext().getPlayer(creator);
                }

                if (!created.isEnchantment()) {
                    playContext.addCardCreation(creatorController, createdController, beneficiary, creator, created);
                }
                return true;
            }
        }
        return false;
    }
}