package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameContext;

import java.util.Objects;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

public class JoustHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity() != null &&
               gameContext.getActivity().isJoust();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        String controller1Id = null;
        String controller2Id = null;

        String controller1CardCost = null;
        String controller2CardCost = null;

        for (Activity a: gameContext.getActivity().getChildren()) {

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
        Card card1 = gameContext.getGameState().getEntityById(controller1Id);
        Card card2 = gameContext.getGameState().getEntityById(controller2Id);

        if (Objects.equals(gameContext.getGameState().getFriendlyPlayer().getController(), controller1Id)) {
            addJoustResult(gameContext, cost1, cost2, card1, card2);
        } else {
            addJoustResult(gameContext, cost2, cost1, card2, card1);
        }
        return true;
    }

    private void addJoustResult(GameContext gameContext, int cost1, int cost2, Card card1, Card card2) {
        boolean winner = cost1 > cost2;
        gameContext.addJoust(gameContext.getGameState().getFriendlyPlayer(), gameContext.getGameState().getOpposingPlayer(), card1, card2, card1, winner);

    }
}
