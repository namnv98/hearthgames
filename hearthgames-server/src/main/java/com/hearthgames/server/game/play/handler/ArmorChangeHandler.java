package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class ArmorChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               (playContext.getActivity().getDelta() instanceof Card) &&
               playContext.getContext().getAfter(playContext.getActivity()).getArmor() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        int armor = Integer.parseInt(after.getArmor());
        Player player = playContext.getContext().getPlayer(before);
        playContext.addArmorChange(player, before, armor);

        return true;
    }
}
