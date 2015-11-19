package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import org.springframework.stereotype.Component;

@Component
public class PlayerHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof Player;
    }

    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Player before = (Player) context.getEntityById(activity.getEntityId());
        Player after = (Player) activity.getDelta();


        if (after.getNumOptions() != null) {
            result.addNumOptions(Integer.parseInt(after.getNumOptions()));
//            System.out.println(result.getCurrentTurn().getWhoseTurn().getName() + " has " + after.getNumOptions() + " possible moves to make.");
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
            result.addManaGained(Integer.parseInt(after.getResources()));
        } else if (after.getResourcesUsed() != null && !"0".equals(after.getResourcesUsed()) && before == result.getCurrentTurn().getWhoseTurn()) {
            Entity usedOn = activity.getParent().getDelta();

            int manaUsed = Integer.parseInt(after.getResourcesUsed()) - result.getCurrentTurn().getManaUsed();
            if (result.getCurrentTurn().getTempManaUsed() > 0) {
                manaUsed += result.getCurrentTurn().getTempManaUsed();
                result.getCurrentTurn().setTempManaUsed(0);
            }
            result.addManaUsed(usedOn, manaUsed);

            if (activity.getParent().getDelta() instanceof Card) {
                Card cardUsedOn = (Card) usedOn;
                CardDetails cardDetails = cardUsedOn.getCardDetails();
                if (cardDetails != null) {
                    int cost = Integer.parseInt(cardDetails.getCost());
                    if (manaUsed < cost) {
                        result.addManaSaved(cardUsedOn, cost - manaUsed);
                    } else if (manaUsed > cost){
                        result.addManaLost(cardUsedOn, manaUsed - cost);
                    }
                }
            }
        } else if ((before.getTempResources() == null || before.getTempResources().equals("0")) && after.getTempResources() != null) {
            Card fromCard = (Card) activity.getParent().getDelta();
            result.addTempManaGained(fromCard, Integer.parseInt(after.getTempResources()));
        } else if (before.getTempResources() != null && after.getTempResources() != null) {
            int tempManaUsed = Integer.parseInt(before.getTempResources()) - Integer.parseInt(after.getTempResources());
            result.getCurrentTurn().setTempManaUsed(tempManaUsed);
        }
        return true;
    }
}