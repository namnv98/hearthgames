package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;

public class AttackHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity() != null && playContext.getActivity().isAttack();
    }

    @Override
    public boolean handle(PlayContext playContext) {

        Card attacker = (Card) playContext.getContext().getEntityById(playContext.getActivity().getDelta().getEntityId());
        Card defender = (Card) playContext.getContext().getEntityById(playContext.getActivity().getTarget().getEntityId());

        Player attackerController = attacker.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();
        Player defenderController = defender.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();

        playContext.addAttack(attacker, defender, attackerController, defenderController);

        return false;
    }
}
