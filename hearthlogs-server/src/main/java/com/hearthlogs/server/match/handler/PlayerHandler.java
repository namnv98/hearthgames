package com.hearthlogs.server.match.handler;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.CardDetails;
import com.hearthlogs.server.match.domain.Player;
import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import org.springframework.stereotype.Component;

@Component
public class PlayerHandler extends ActivityHandler {

    protected void handleTagChange(MatchResult result, MatchContext context, Activity activity, Player before, Player after) {
        if (after.getCurrentPlayer() != null && TRUE_OR_ONE.equals(after.getCurrentPlayer())) {
            result.getCurrentTurn().setWhoseTurn(before);
        }

        if (after.getPlaystate() != null && Player.PlayState.QUIT.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has quit.");
            result.setQuitter(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.WON.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has won.");
            result.setWinner(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.LOST.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has lost.");
            result.setLoser(before);
        }
        if (TRUE_OR_ONE.equals(before.getFirstPlayer())) {
            result.setFirst(before);
        }

        if (after.getResources() != null) {
            result.getCurrentTurn().addManaGained(Integer.parseInt(after.getResources()));
        } else if (after.getResourcesUsed() != null && !"0".equals(after.getResourcesUsed())) {
            Card cardUsedOn = (Card) activity.getParent().getEntity();

            int manaUsed = Integer.parseInt(after.getResourcesUsed()) - result.getCurrentTurn().getManaUsed();
            if (result.getCurrentTurn().getTempManaUsed() > 0) {
                manaUsed += result.getCurrentTurn().getTempManaUsed();
                result.getCurrentTurn().setTempManaUsed(0);
            }
            result.getCurrentTurn().addManaUsed(cardUsedOn, manaUsed);

            if (activity.getParent().getEntity() instanceof Card) {
                CardDetails cardDetails = cardUsedOn.getCardDetails();
//                System.out.println(cardDetails.getName() + " costs : " + cardDetails.getCost());

                int cost = Integer.parseInt(cardDetails.getCost());
                if (manaUsed < cost) {
                    result.getCurrentTurn().addManaSaved(cardUsedOn, cost - manaUsed);
                } else if (manaUsed > cost){
                    result.getCurrentTurn().addManaOverspent(cardUsedOn, manaUsed - cost);
                }
            }

        } else if ((before.getTempResources() == null || before.getTempResources().equals("0")) && after.getTempResources() != null) {
            Card fromCard = (Card) activity.getParent().getEntity();
            result.getCurrentTurn().addTempManaGained(fromCard, Integer.parseInt(after.getTempResources()));
        } else if (before.getTempResources() != null && after.getTempResources() != null) {
            int tempManaUsed = Integer.parseInt(before.getTempResources()) - Integer.parseInt(after.getTempResources());
            result.getCurrentTurn().setTempManaUsed(tempManaUsed);
        }
    }
}