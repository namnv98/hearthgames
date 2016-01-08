package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class HeroPowerHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isPower() && playContext.getActivity().getDelta() instanceof Card;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card card = playContext.getContext().getBefore(playContext.getActivity());
        Player player = playContext.getContext().getPlayerForCard(card);

        if (Card.Type.HERO_POWER.eq(card.getCardtype())) {
            playContext.addHeroPowerUsed(player, card);
            return true;
        }
        return false;
    }
}
