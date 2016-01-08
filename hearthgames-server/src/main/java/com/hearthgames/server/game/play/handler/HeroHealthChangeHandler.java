package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class HeroHealthChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && (playContext.getActivity().getDelta() instanceof Card)
                && playContext.getContext().getAfter(playContext.getActivity()).getDamage() != null
                && !FALSE_OR_ZERO.equals(playContext.getContext().getAfter(playContext.getActivity()).getDamage())
                && Card.Type.HERO.eq(playContext.getContext().getBefore(playContext.getActivity()).getCardtype());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        int health = before.getCardDetailsHealth();
        int damage = Integer.parseInt(after.getDamage());
        int newHealth = health - damage;

        Player player = playContext.getContext().getPlayer(before);
        playContext.addHeroHealthChange(player, before, newHealth);
        return true;
    }
}
