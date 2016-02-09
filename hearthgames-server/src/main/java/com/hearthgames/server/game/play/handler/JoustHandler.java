package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;

import java.util.Objects;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class JoustHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity() != null &&
               playContext.getActivity().isJoust();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        String controller1Id = null;
        String controller2Id = null;

        String controller1CardCost = null;
        String controller2CardCost = null;

        for (Activity a: playContext.getActivity().getChildren()) {

            if (a.isNewCard()) {
                Card card = a.getDelta();
                if (TRUE_OR_ONE.equals(card.getController())) {
                    controller1Id = card.getEntityId();
                } else {
                    controller2Id = card.getEntityId();
                }
            } else if (a.isShowEntity()) {
                Card card = a.getDelta();
                if (card.getEntityId().equals(controller1Id)) {
                    controller1CardCost = card.getCost();
                } else {
                    controller2CardCost = card.getCost();
                }
            }
        }
        int cost1 = controller1CardCost != null ? Integer.parseInt(controller1CardCost) : 0;
        int cost2 = controller2CardCost != null ? Integer.parseInt(controller2CardCost) : 0;
        Card card1 = playContext.getContext().getEntityById(controller1Id);
        Card card2 = playContext.getContext().getEntityById(controller2Id);

        if (Objects.equals(playContext.getContext().getFriendlyPlayer().getController(), controller1Id)) {
            addJoustResult(playContext, cost1, cost2, card1, card2);
        } else {
            addJoustResult(playContext, cost2, cost1, card2, card1);
        }
        return true;
    }

    private void addJoustResult(PlayContext playContext,  int cost1, int cost2, Card card1, Card card2) {
        boolean winner = cost1 > cost2;
        playContext.addJoust(playContext.getContext().getFriendlyPlayer(), playContext.getContext().getOpposingPlayer(), card1, card2, card1, winner);

    }
}
