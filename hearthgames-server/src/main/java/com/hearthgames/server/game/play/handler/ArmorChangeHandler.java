package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

public class ArmorChangeHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getArmor() != null;
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        int armor = Integer.parseInt(after.getArmor());
        Player player = gameContext.getGameState().getPlayer(before);
        gameContext.addArmorChange(player, before, armor);

        return true;
    }
}
