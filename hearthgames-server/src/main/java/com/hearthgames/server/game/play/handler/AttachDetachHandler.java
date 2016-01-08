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
            Entity entity = playContext.getContext().getEntityById(before.getAttached());
            if (entity instanceof Card) {
                Card detachFrom = (Card) entity;
                playContext.addDetached(before, detachFrom);
                return true;
            }
        } else {
            Entity entity = playContext.getContext().getEntityById(after.getAttached());
            if (entity instanceof Card) {
                Card attachTo = (Card) entity;
                playContext.addAttached(before, attachTo);
                return true;
            }
        }
        return false;
    }
}
