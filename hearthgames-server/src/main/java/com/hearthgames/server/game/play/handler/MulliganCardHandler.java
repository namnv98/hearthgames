package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class MulliganCardHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return isHideEntityOrTagChange(playContext) &&
               playContext.getActivity().isCard() &&
               isCardFromHandToDeck(playContext) &&
               Player.DEALING.equals(playContext.getContext().getPlayerForCard(playContext.getBefore()).getMulliganState());
    }

    private boolean isHideEntityOrTagChange(PlayContext playContext) {
        return playContext.getActivity().isHideEntity() || playContext.getActivity().isTagChange();
    }

    private boolean isCardFromHandToDeck(PlayContext playContext) {
        return playContext.getAfter().getZone() != null && Zone.HAND.eq(playContext.getBefore().getZone()) && Zone.DECK.eq(playContext.getAfter().getZone());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
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
