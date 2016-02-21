package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameContext;

public class MulliganCardHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return isHideEntityOrTagChange(gameContext) &&
               gameContext.getActivity().isCard() &&
               isCardFromHandToDeck(gameContext) &&
               Player.DEALING.equals(gameContext.getGameState().getPlayerForCard(gameContext.getBefore()).getMulliganState());
    }

    private boolean isHideEntityOrTagChange(GameContext gameContext) {
        return gameContext.getActivity().isHideEntity() || gameContext.getActivity().isTagChange();
    }

    private boolean isCardFromHandToDeck(GameContext gameContext) {
        return gameContext.getAfter().getZone() != null && Zone.HAND.eq(gameContext.getBefore().getZone()) && Zone.DECK.eq(gameContext.getAfter().getZone());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Player player = gameContext.getGameState().getPlayerForCard(before);

        if (gameContext.getGameState().isFriendly(player)) {
            gameContext.getResult().mulliganFriendlyCard(before);
        } else {
            gameContext.getResult().mulliganOpposingCard(before);
        }
        gameContext.addLoggingAction(player.getName() + " has mulliganed " + before.getCardDetailsName());
        return true;
    }
}
