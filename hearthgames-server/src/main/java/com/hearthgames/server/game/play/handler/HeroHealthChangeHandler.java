package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class HeroHealthChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               playContext.getBefore().isHero() &&
               isThereDamage(playContext);
    }

    private boolean isThereDamage(PlayContext playContext) {
        return playContext.getAfter().getDamage() != null &&
               !FALSE_OR_ZERO.equals(playContext.getAfter().getDamage());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        int health = before.getCardDetailsHealth();
        int damage = Integer.parseInt(after.getDamage());
        int newHealth = health - damage;

        Player player = playContext.getContext().getPlayer(before);
        playContext.addHeroHealthChange(player, before, newHealth);
        return true;
    }
}
