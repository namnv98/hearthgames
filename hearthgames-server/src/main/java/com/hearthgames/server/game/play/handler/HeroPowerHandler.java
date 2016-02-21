package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

public class HeroPowerHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isPower() &&
               gameContext.getActivity().isCard() &&
               gameContext.getBefore().isHeroPower();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card card = gameContext.getBefore();
        Player player = gameContext.getGameState().getPlayerForCard(card);

        gameContext.addHeroPowerUsed(player, card);
        return true;
    }
}
