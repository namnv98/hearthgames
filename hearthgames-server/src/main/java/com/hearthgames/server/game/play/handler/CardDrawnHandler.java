package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameContext;

public class CardDrawnHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return isShowEntityOrTagChange(gameContext) &&
               gameContext.getActivity().isCard() &&
               isCardFromDeckToHand(gameContext);
    }

    private boolean isShowEntityOrTagChange(GameContext gameContext) {
        return gameContext.getActivity().isShowEntity() ||
               gameContext.getActivity().isTagChange();
    }

    private boolean isCardFromDeckToHand(GameContext gameContext) {
        return gameContext.getAfter().getZone() != null &&
               Zone.DECK.eq(gameContext.getBefore().getZone()) &&
               Zone.HAND.eq(gameContext.getAfter().getZone());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Player player = gameContext.getGameState().getPlayerForCard(before);

        if (gameContext.getGameState().getGameEntity().isMulliganOver()) {
            if (gameContext.getGameState().getStartingCardIds().contains(before.getEntityId())) {
                if (player == gameContext.getGameState().getFriendlyPlayer()) {
                    gameContext.getResult().addFriendlyDeckCard(before);
                } else {
                    gameContext.getResult().addOpposingDeckCard(before);
                }
            }
            gameContext.addCardDrawn(player, before, gameContext.getActivity().getParent().getDelta());
        } else {
            if (player == gameContext.getGameState().getFriendlyPlayer()) {
                if (gameContext.getGameState().getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                    gameContext.getResult().addFriendlyStartingCard(before);
                    gameContext.addLoggingAction(player.getName() + " has drawn " +  before.getCardDetailsName());
                    return true;
                }
            } else {
                if (gameContext.getGameState().getStartingCardIds().contains(before.getEntityId())) {
                    gameContext.getResult().addOpposingStartingCard(before);
                    gameContext.addLoggingAction(player.getName() + " has drawn " +  before.getCardDetailsName());
                    return true;
                }
            }
        }
        return false;
    }
}
