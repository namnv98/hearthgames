package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Entity;
import com.hearthgames.server.game.play.PlayContext;

public class AttachDetachHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && (playContext.getActivity().getDelta() instanceof Card) && playContext.getContext().getAfter(playContext.getActivity()).getAttached() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        if (FALSE_OR_ZERO.equals(after.getAttached())) {
            Card detachFrom = playContext.getContext().getCardByEntityId(before.getAttached());
            if (detachFrom != null) {
                playContext.addDetached(before, detachFrom);
                return true;
            }
        } else {
            Card attachTo = playContext.getContext().getCardByEntityId(after.getAttached());
            if (attachTo != null) {
                playContext.addAttached(before, attachTo);
                return true;
            }
        }
        return false;
    }
}
