package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class HealthChangeHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && (playContext.getActivity().getDelta() instanceof Card) && playContext.getContext().getAfter(playContext.getActivity()).getHealth() != null && Zone.PLAY.eq(playContext.getContext().getBefore(playContext.getActivity()).getZone());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        int newHealth = Integer.parseInt(after.getHealth());
        int currentHealth = before.getHealth() == null ? before.getCardDetailsHealth() : Integer.parseInt(before.getHealth());

        int diffHealth = newHealth - currentHealth;
        Player player = playContext.getContext().getPlayer(before);
        playContext.addHealthChange(player, before, diffHealth, newHealth);

        return true;
    }
}
