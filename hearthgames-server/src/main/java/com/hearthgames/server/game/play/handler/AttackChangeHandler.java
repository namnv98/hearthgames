package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class AttackChangeHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               playContext.getAfter().getAtk() != null &&
               Zone.PLAY.eq(playContext.getBefore().getZone());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        int newAttack = Integer.parseInt(after.getAtk());
        int currentAttack = before.getAtk() == null ? before.getCardDetailsAttack() : Integer.parseInt(before.getAtk());

        int diffAttack = newAttack - currentAttack;
        Player player = playContext.getContext().getPlayer(before);
        playContext.addAttackChange(player, before, diffAttack, newAttack);

        return true;
    }
}
