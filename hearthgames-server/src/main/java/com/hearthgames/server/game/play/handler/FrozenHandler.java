package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class FrozenHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               playContext.getAfter().getFrozen() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        boolean frozen = TRUE_OR_ONE.equals(after.getFrozen());
        Player player = playContext.getContext().getPlayer(before);
        playContext.addFrozen(player, before, frozen);

        return true;
    }
}
