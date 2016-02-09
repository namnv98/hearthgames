package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class AttachDetachHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               playContext.getAfter().getAttached() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        if (FALSE_OR_ZERO.equals(after.getAttached())) {
            Card detachFrom = playContext.getContext().getEntityById(before.getAttached());
            if (detachFrom != null) {
                playContext.addDetached(before, detachFrom);
                return true;
            }
        } else {
            Card attachTo = playContext.getContext().getEntityById(after.getAttached());
            if (attachTo != null) {
                playContext.addAttached(before, attachTo);
                return true;
            }
        }
        return false;
    }
}
