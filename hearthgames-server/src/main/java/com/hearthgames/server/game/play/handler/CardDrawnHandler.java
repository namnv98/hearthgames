package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class CardDrawnHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return isShowEntityOrTagChange(playContext) &&
               playContext.getActivity().isCard() &&
               isCardFromDeckToHand(playContext);
    }

    private boolean isShowEntityOrTagChange(PlayContext playContext) {
        return playContext.getActivity().isShowEntity() ||
               playContext.getActivity().isTagChange();
    }

    private boolean isCardFromDeckToHand(PlayContext playContext) {
        return playContext.getAfter().getZone() != null &&
               Zone.DECK.eq(playContext.getBefore().getZone()) &&
               Zone.HAND.eq(playContext.getAfter().getZone());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Player player = playContext.getContext().getPlayerForCard(before);

        if (playContext.getContext().getGameEntity().isMulliganOver()) {
            if (playContext.getContext().getStartingCardIds().contains(before.getEntityId())) {
                if (player == playContext.getContext().getFriendlyPlayer()) {
                    playContext.getResult().addFriendlyDeckCard(before);
                } else {
                    playContext.getResult().addOpposingDeckCard(before);
                }
            }
            playContext.addCardDrawn(player, before, playContext.getActivity().getParent().getDelta());
        } else {
            if (player == playContext.getContext().getFriendlyPlayer()) {
                if (playContext.getContext().getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                    playContext.getResult().addFriendlyStartingCard(before);
                    playContext.addLoggingAction(player.getName() + " has drawn " +  before.getCardDetailsName());
                    return true;
                }
            } else {
                if (playContext.getContext().getStartingCardIds().contains(before.getEntityId())) {
                    playContext.getResult().addOpposingStartingCard(before);
                    playContext.addLoggingAction(player.getName() + " has drawn " +  before.getCardDetailsName());
                    return true;
                }
            }
        }
        return false;
    }
}
