package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class MulliganCardHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return (playContext.getActivity().isHideEntity() || playContext.getActivity().isTagChange()) && playContext.getActivity().getDelta() instanceof Card && playContext.getContext().getAfter(playContext.getActivity()).getZone() != null && Zone.HAND.eq(playContext.getContext().getBefore(playContext.getActivity()).getZone()) && Zone.DECK.eq(playContext.getContext().getAfter(playContext.getActivity()).getZone()) && Player.DEALING.equals(playContext.getContext().getPlayerForCard(playContext.getContext().getBefore(playContext.getActivity())).getMulliganState());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Player player = playContext.getContext().getPlayerForCard(before);

        if (playContext.getContext().isFriendly(player)) {
            playContext.getResult().mulliganFriendlyCard(before);
        } else {
            playContext.getResult().mulliganOpposingCard(before);
        }
        playContext.addLoggingAction(player.getName() + " has mulliganed " + before.getCardDetailsName());
        return true;
    }
}
