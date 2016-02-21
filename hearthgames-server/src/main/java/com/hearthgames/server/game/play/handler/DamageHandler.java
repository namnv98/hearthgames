package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameContext;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;

public class DamageHandler implements Handler {
    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               !gameContext.getBefore().isWeapon() &&
               isDamageDone(gameContext);
    }

    private boolean isDamageDone(GameContext gameContext) {
        return gameContext.getAfter().getPredamage() != null &&
               !FALSE_OR_ZERO.equals(gameContext.getAfter().getPredamage());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        Card attacker = gameContext.getGameState().getEntityById(gameContext.getGameState().getGameEntity().getProposedAttacker());
        Card defender = gameContext.getGameState().getEntityById(gameContext.getGameState().getGameEntity().getProposedDefender());
        if (attacker == null && defender == null) {
            if (gameContext.getActivity().getParent().getTarget() != null) {
                attacker = gameContext.getActivity().getParent().getDelta();
                defender = gameContext.getActivity().getParent().getTarget();
            } else {
                attacker = gameContext.getActivity().getParent().getDelta();
                defender = before;
            }
        }

        int damage = Integer.parseInt(after.getPredamage());
        if (before == attacker) {
            if (defender == null) {
                gameContext.addDamage(gameContext.getGameState().getPlayer(attacker), gameContext.getGameState().getPlayer(before), attacker, before, damage);
                return true;
            } else {
                gameContext.addDamage(gameContext.getGameState().getPlayer(defender), gameContext.getGameState().getPlayer(before), defender, before, damage);
                return true;
            }
        } else if (before == defender) {
            gameContext.addDamage(gameContext.getGameState().getPlayer(attacker), gameContext.getGameState().getPlayer(before), attacker, before, damage);
            return true;
        } else {
            gameContext.addDamage(gameContext.getGameState().getPlayer(attacker), gameContext.getGameState().getPlayer(before), attacker, before, damage);
            return true;
        }
    }
}
