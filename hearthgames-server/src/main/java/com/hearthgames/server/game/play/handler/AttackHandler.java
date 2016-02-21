package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.GameEntity;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;

public class AttackHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        if (gameContext.getActivity().isTagChange() && gameContext.getActivity().isGame()) {
            GameEntity after = (GameEntity) gameContext.getActivity().getDelta();
            if (after.getStep() != null && GameEntity.Step.MAIN_COMBAT.eq(after.getStep())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(GameContext gameContext) {

        Activity attack = getParentAttackActivity(gameContext.getActivity());
        if (attack != null) {
            Card attacker = gameContext.getGameState().getEntityById(gameContext.getGameState().getGameEntity().getProposedAttacker());
            Card defender = gameContext.getGameState().getEntityById(gameContext.getGameState().getGameEntity().getProposedDefender());
            Player attackerController = null;
            if (attacker.getController().equals(gameContext.getGameState().getFriendlyPlayer().getController())) {
                attackerController = gameContext.getGameState().getFriendlyPlayer();
            } else if (attacker.getController().equals(gameContext.getGameState().getOpposingPlayer().getController())) {
                attackerController = gameContext.getGameState().getOpposingPlayer();
            }

            Player defenderController = defender.getController().equals(gameContext.getGameState().getFriendlyPlayer().getController()) ? gameContext.getGameState().getFriendlyPlayer() : gameContext.getGameState().getOpposingPlayer();

            Card originalDefender = gameContext.getGameState().getEntityById(attack.getTarget().getEntityId());
            if (originalDefender == defender) {
                gameContext.addAttack(attacker, defender, attackerController, defenderController);
            } else {
                Player originalDefenderController = originalDefender.getController().equals(gameContext.getGameState().getFriendlyPlayer().getController()) ? gameContext.getGameState().getFriendlyPlayer() : gameContext.getGameState().getOpposingPlayer();
                gameContext.addAttack(attacker, defender, originalDefender, attackerController, defenderController, originalDefenderController);
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
