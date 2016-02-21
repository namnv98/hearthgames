package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class AttachDetachHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getAttached() != null;
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        if (FALSE_OR_ZERO.equals(after.getAttached())) {
            Card detachFrom = gameContext.getGameState().getEntityById(before.getAttached());
            if (detachFrom != null) {
                gameContext.addDetached(before, detachFrom);
                return true;
            }
        } else {
            Card attachTo = gameContext.getGameState().getEntityById(after.getAttached());
            if (attachTo != null) {
                gameContext.addAttached(before, attachTo);
                return true;
            }
        }
        return false;
    }
}
