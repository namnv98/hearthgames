package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class DamageHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isCard() &&
               !playContext.getBefore().isWeapon() &&
               isDamageDone(playContext);
    }

    private boolean isDamageDone(PlayContext playContext) {
        return playContext.getAfter().getPredamage() != null &&
               !FALSE_OR_ZERO.equals(playContext.getAfter().getPredamage());
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getBefore();
        Card after = playContext.getAfter();

        Card attacker = playContext.getContext().getEntityById(playContext.getContext().getGameEntity().getProposedAttacker());
        Card defender = playContext.getContext().getEntityById(playContext.getContext().getGameEntity().getProposedDefender());
        if (attacker == null && defender == null) {
            if (playContext.getActivity().getParent().getTarget() != null) {
                attacker = playContext.getActivity().getParent().getDelta();
                defender = playContext.getActivity().getParent().getTarget();
            } else {
                attacker = playContext.getActivity().getParent().getDelta();
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
