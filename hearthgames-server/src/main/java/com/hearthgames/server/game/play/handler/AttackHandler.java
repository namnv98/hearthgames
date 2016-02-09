package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.GameEntity;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class AttackHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        if (playContext.getActivity().isTagChange() && playContext.getActivity().isGame()) {
            GameEntity after = (GameEntity) playContext.getActivity().getDelta();
            if (after.getStep() != null && GameEntity.Step.MAIN_COMBAT.eq(after.getStep())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(PlayContext playContext) {

        Activity attack = getParentAttackActivity(playContext.getActivity());
        if (attack != null) {
            Card attacker = playContext.getContext().getEntityById(playContext.getContext().getGameEntity().getProposedAttacker());
            Card defender = playContext.getContext().getEntityById(playContext.getContext().getGameEntity().getProposedDefender());
            Player attackerController = null;
            if (attacker.getController().equals(playContext.getContext().getFriendlyPlayer().getController())) {
                attackerController = playContext.getContext().getFriendlyPlayer();
            } else if (attacker.getController().equals(playContext.getContext().getOpposingPlayer().getController())) {
                attackerController = playContext.getContext().getOpposingPlayer();
            }

            Player defenderController = defender.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();

            Card originalDefender = playContext.getContext().getEntityById(attack.getTarget().getEntityId());
            if (originalDefender == defender) {
                playContext.addAttack(attacker, defender, attackerController, defenderController);
            } else {
                Player originalDefenderController = originalDefender.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();
                playContext.addAttack(attacker, defender, originalDefender, attackerController, defenderController, originalDefenderController);
                return true;
            }
        }
        return false;
    }

    private Activity getParentAttackActivity(Activity activity) {
        if (activity == null) {
            return null;
        }
        if (activity.isAttack()) {
            return activity;
        }
        return getParentAttackActivity(activity.getParent());
    }
}
