package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class TriggerHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTrigger() && playContext.getActivity().getDelta() instanceof Card && playContext.getActivity().getChildren().size() > 0;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card card = playContext.getContext().getBefore(playContext.getActivity());
        Player cardController = card.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();
        playContext.addTrigger(cardController, card);
        return true;
    }
}
