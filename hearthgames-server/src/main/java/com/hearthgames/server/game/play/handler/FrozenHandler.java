package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class FrozenHandler implements Handler {
    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getFrozen() != null;
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        boolean frozen = TRUE_OR_ONE.equals(after.getFrozen());
        Player player = gameContext.getGameState().getPlayer(before);
        gameContext.addFrozen(player, before, frozen);

        return true;
    }
}
