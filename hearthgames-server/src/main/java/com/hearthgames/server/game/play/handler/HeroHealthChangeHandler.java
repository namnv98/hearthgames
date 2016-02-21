package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class HeroHealthChangeHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getBefore().isHero() &&
               isThereDamage(gameContext);
    }

    private boolean isThereDamage(GameContext gameContext) {
        return gameContext.getAfter().getDamage() != null &&
               !FALSE_OR_ZERO.equals(gameContext.getAfter().getDamage());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        int health = before.getCardDetailsHealth();
        int damage = Integer.parseInt(after.getDamage());
        int newHealth = health - damage;

        Player player = gameContext.getGameState().getPlayer(before);
        gameContext.addHeroHealthChange(player, before, newHealth);
        return true;
    }
}
