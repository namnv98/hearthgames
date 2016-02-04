package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;

public class DamageHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange()
                && (playContext.getActivity().getDelta() instanceof Card)
                && !playContext.getContext().getBefore(playContext.getActivity()).isWeapon()
                && playContext.getContext().getAfter(playContext.getActivity()).getPredamage() != null
                && !FALSE_OR_ZERO.equals(playContext.getContext().getAfter(playContext.getActivity()).getPredamage());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        Card attacker = playContext.getContext().getCardByEntityId(playContext.getContext().getGameEntity().getProposedAttacker());
        Card defender = playContext.getContext().getCardByEntityId(playContext.getContext().getGameEntity().getProposedDefender());
        if (attacker == null && defender == null) {
            if (playContext.getActivity().getParent().getDelta() instanceof Card && playContext.getActivity().getParent().getTarget() != null && playContext.getActivity().getParent().getTarget() instanceof Card) {
                attacker = (Card) playContext.getActivity().getParent().getDelta();
                defender = (Card) playContext.getActivity().getParent().getTarget();
            } else {
                attacker = (Card) playContext.getActivity().getParent().getDelta();
                defender = before;
            }
        }

        int damage = Integer.parseInt(after.getPredamage());
        if (before == attacker) {
            if (defender == null) {
                playContext.addDamage(playContext.getContext().getPlayer(attacker), playContext.getContext().getPlayer(before), attacker, before, damage);
                return true;
            } else {
                playContext.addDamage(playContext.getContext().getPlayer(defender), playContext.getContext().getPlayer(before), defender, before, damage);
                return true;
            }
        } else if (before == defender) {
            playContext.addDamage(playContext.getContext().getPlayer(attacker), playContext.getContext().getPlayer(before), attacker, before, damage);
            return true;
        } else {
            playContext.addDamage(playContext.getContext().getPlayer(attacker), playContext.getContext().getPlayer(before), attacker, before, damage);
            return true;
        }
    }
}
