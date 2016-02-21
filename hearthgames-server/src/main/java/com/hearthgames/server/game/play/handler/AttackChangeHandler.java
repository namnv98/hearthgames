package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameContext;

public class AttackChangeHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getAtk() != null &&
               Zone.PLAY.eq(gameContext.getBefore().getZone());
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();

        int newAttack = Integer.parseInt(after.getAtk());
        int currentAttack = before.getAtk() == null ? before.getCardDetailsAttack() : Integer.parseInt(before.getAtk());

        int diffAttack = newAttack - currentAttack;
        Player player = gameContext.getGameState().getPlayer(before);
        gameContext.addAttackChange(player, before, diffAttack, newAttack);

        return true;
    }
}
