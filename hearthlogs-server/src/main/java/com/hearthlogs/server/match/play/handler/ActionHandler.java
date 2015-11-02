package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.ActionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ActionHandler extends ActivityHandler {

    @Autowired
    public ActionHandler(ActionFactory actionFactory) {
        super(actionFactory);
    }

    protected void handleAction(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card source, Card target) {
        if (activity.isTrigger()) {
            if (activity.getChildren().size() > 0) {
                result.getCurrentTurn().addTrigger(source);

                System.out.println(source.getCardDetails().getName() + " has triggered: " + source.getCardDetails().getText());
            }


        } else if (activity.isPower()) {

            Card card = (Card) parsedMatch.getEntityById(activity.getEntityId());

            if (Card.Type.HERO_POWER.eq(card.getCardtype())) {
                System.out.println(player.getName() + " has activated their hero power");
                result.getCurrentTurn().addHeroPowerUsed(player, card);
            }

        } else if (activity.isJoust()) {

            String controller1Id = null;
            String controller2Id = null;

            String controller1CardCost = null;
            String controller2CardCost = null;

            String cardId1 = null;
            String cardId2 = null;

            for (Activity a: activity.getChildren()) {

                if (a.isNewCard()) {
                    Card card = (Card) a.getEntity();
                    if (TRUE_OR_ONE.equals(card.getController())) {
                        controller1Id = card.getEntityId();
                    } else {
                        controller2Id = card.getEntityId();
                    }
                } else if (a.isShowEntity()) {
                    Card card = (Card) a.getEntity();
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
                Card card1 = (Card) parsedMatch.getEntityById(controller1Id);
                Card card2 = (Card) parsedMatch.getEntityById(controller2Id);

                if (Objects.equals(parsedMatch.getFriendlyPlayer().getController(), controller1Id)) {
                    addJoustResult(result, parsedMatch, cost1, cost2, card1, card2);
                } else {
                    addJoustResult(result, parsedMatch, cost2, cost1, card2, card1);
                }
            }
        }
    }

    private void addJoustResult(MatchResult result, ParsedMatch parsedMatch, int cost1, int cost2, Card card1, Card card2) {
        System.out.println(card1.getCardDetails().getName() + " (" + cost1 + ")" + " is jousting : " + card2.getCardDetails().getName() + " (" + cost2 + ") ");
        boolean winner = cost1 > cost2;
        if (winner) {
            System.out.println(card1.getCardDetails().getName() + " has won the joust!");
        } else {
            System.out.println(card1.getCardDetails().getName() + " has lost the joust!");
        }
        result.getCurrentTurn().addJoust(parsedMatch.getFriendlyPlayer(), parsedMatch.getOpposingPlayer(), card1.getCardid(), card2.getCardid(), card1, winner);

    }
}
