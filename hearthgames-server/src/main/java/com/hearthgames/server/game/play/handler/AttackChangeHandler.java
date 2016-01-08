package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class AttackChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && (playContext.getActivity().getDelta() instanceof Card) && playContext.getContext().getAfter(playContext.getActivity()).getAtk() != null && Zone.PLAY.eq(playContext.getContext().getBefore(playContext.getActivity()).getZone());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        int newAttack = Integer.parseInt(after.getAtk());
        int currentAttack = before.getAtk() == null ? before.getCardDetailsAttack() : Integer.parseInt(before.getAtk());

        int diffAttack = newAttack - currentAttack;
        Player player = playContext.getContext().getPlayer(before);
        playContext.addAttackChange(player, before, diffAttack, newAttack);

        return true;
    }
}
