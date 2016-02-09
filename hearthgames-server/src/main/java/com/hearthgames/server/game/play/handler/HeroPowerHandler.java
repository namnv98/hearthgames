package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class HeroPowerHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isPower() &&
               playContext.getActivity().isCard() &&
               playContext.getBefore().isHeroPower();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card card = playContext.getBefore();
        Player player = playContext.getContext().getPlayerForCard(card);

        playContext.addHeroPowerUsed(player, card);
        return true;
    }
}
