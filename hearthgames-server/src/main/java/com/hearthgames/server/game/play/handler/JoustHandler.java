package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameResult;

import java.util.Objects;

public class JoustHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity != null && activity.isJoust();
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        String controller1Id = null;
        String controller2Id = null;

        String controller1CardCost = null;
        String controller2CardCost = null;

        String cardId1 = null;
        String cardId2 = null;

        for (Activity a: activity.getChildren()) {

            if (a.isNewCard()) {
                Card card = (Card) a.getDelta();
                if (TRUE_OR_ONE.equals(card.getController())) {
                    controller1Id = card.getEntityId();
                } else {
                    controller2Id = card.getEntityId();
                }
            } else if (a.isShowEntity()) {
                Card card = (Card) a.getDelta();
                if (card.getEntityId().equals(controller1Id)) {
                    controller1CardCost = card.getCost();
                    cardId1 = card.getCardid();
                } else {
                    controller2CardCost = card.getCost();
                    cardId2 = card.getCardid();
                }
            }
            if (controller1Id != null && controller2Id != null && controller1CardCost != null && controller2CardCost != null &&
                    cardId1 != null && cardId2 != null) {
                break;
            }
        }
        if (controller1Id != null && controller2Id != null && controller1CardCost != null && controller2CardCost != null &&
                cardId1 != null && cardId2 != null) {
            int cost1 = Integer.parseInt(controller1CardCost);
            int cost2 = Integer.parseInt(controller2CardCost);
            Card card1 = (Card) context.getEntityById(controller1Id);
            Card card2 = (Card) context.getEntityById(controller2Id);

            if (Objects.equals(context.getFriendlyPlayer().getController(), controller1Id)) {
                addJoustResult(result, context, cost1, cost2, card1, card2);
            } else {
                addJoustResult(result, context, cost2, cost1, card2, card1);
            }
            return true;
        }
        return false;
    }

    private void addJoustResult(GameResult result, GameContext context, int cost1, int cost2, Card card1, Card card2) {
        boolean winner = cost1 > cost2;
        result.addJoust(context.getFriendlyPlayer(), context.getOpposingPlayer(), card1, card2, card1, winner);

    }
}
