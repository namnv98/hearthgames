package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class ArmorChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               playContext.getAfter().getArmor() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        int armor = Integer.parseInt(after.getArmor());
        Player player = playContext.getContext().getPlayer(before);
        playContext.addArmorChange(player, before, armor);

        return true;
    }
}
