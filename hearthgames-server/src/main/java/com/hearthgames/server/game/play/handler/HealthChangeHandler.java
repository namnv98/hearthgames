package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameContext;

public class HealthChangeHandler implements Handler {
    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getHealth() != null &&
               Zone.PLAY.eq(gameContext.getBefore().getZone());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        int newHealth = Integer.parseInt(after.getHealth());
        int currentHealth = before.getHealth() == null ? before.getCardDetailsHealth() : Integer.parseInt(before.getHealth());

        int diffHealth = newHealth - currentHealth;
        Player player = gameContext.getGameState().getPlayer(before);
        gameContext.addHealthChange(player, before, diffHealth, newHealth);

        return true;
    }
}
