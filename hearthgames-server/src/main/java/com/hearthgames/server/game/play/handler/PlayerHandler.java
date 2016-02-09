package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.CardDetails;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.PlayContext;
import org.springframework.stereotype.Component;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;
import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

@Component
public class PlayerHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && playContext.getActivity().getDelta() instanceof Player;
    }

    public boolean handle(PlayContext playContext) {
        Player before = (Player) playContext.getContext().getEntityById(playContext.getActivity().getEntityId());
        Player after = (Player) playContext.getActivity().getDelta();

        if (after.getNumTurnsLeft() != null && FALSE_OR_ZERO.equals(after.getNumTurnsLeft())) {
            playContext.addEndofTurn();
        }

        if (after.getNumOptions() != null) {
//            playContext.addNumOptions(Integer.parseInt(after.getNumOptions()));
//            playContext.addLoggingAction(playContext.getResult().getCurrentTurn().getWhoseTurn().getName() + " has " + after.getNumOptions() + " possible moves to make.");
        }

        if (after.getPlaystate() != null && (Player.PlayState.QUIT.eq(after.getPlaystate()) || Player.PlayState.CONCEDED.eq(after.getPlaystate()))) {
            playContext.addLoggingAction(before.getName() + " has quit.");
            playContext.getResult().setQuitter(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.WON.eq(after.getPlaystate())) {
            playContext.addLoggingAction(before.getName() + " has won.");
            playContext.getResult().getWinners().add(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.LOST.eq(after.getPlaystate())) {
            playContext.addLoggingAction(before.getName() + " has lost.");
            playContext.getResult().getLosers().add(before);
        }
        if (TRUE_OR_ONE.equals(before.getFirstPlayer())) {
            playContext.getResult().setFirst(before);
        }

        if (after.getResources() != null) {
            playContext.addManaGained(playContext.getResult().getCurrentTurn().getWhoseTurn(), Integer.parseInt(after.getResources()));
        } else if (after.getResourcesUsed() != null && !HandlerConstants.FALSE_OR_ZERO.equals(after.getResourcesUsed()) && before == playContext.getResult().getCurrentTurn().getWhoseTurn()) {
            Card usedOn = playContext.getActivity().getParent().getDelta();

            int manaUsed = Integer.parseInt(after.getResourcesUsed()) - playContext.getResult().getCurrentTurn().getManaUsed();
            if (playContext.getResult().getCurrentTurn().getTempManaUsed() > 0) {
                manaUsed += playContext.getResult().getCurrentTurn().getTempManaUsed();
                playContext.getResult().getCurrentTurn().setTempManaUsed(0);
            }
            playContext.addManaUsed(playContext.getResult().getCurrentTurn().getWhoseTurn(), usedOn, manaUsed);

            if (playContext.getActivity().getParent().getDelta().isCard()) {
                CardDetails cardDetails = usedOn.getCardDetails();
                if (cardDetails != null) {
                    int cost = cardDetails.getCost();
                    if (manaUsed < cost) {
                        playContext.addManaSaved(usedOn, cost - manaUsed);
                    } else if (manaUsed > cost){
                        playContext.addManaLost(usedOn, manaUsed - cost);
                    }
                }
            }
        } else if ((before.getTempResources() == null || HandlerConstants.FALSE_OR_ZERO.equals(before.getTempResources())) && after.getTempResources() != null) {
            Card fromCard = playContext.getActivity().getParent().getDelta();
            playContext.addTempManaGained(playContext.getResult().getCurrentTurn().getWhoseTurn(), fromCard, Integer.parseInt(after.getTempResources()));
        } else if (before.getTempResources() != null && after.getTempResources() != null) {
            int tempManaUsed = Integer.parseInt(before.getTempResources()) - Integer.parseInt(after.getTempResources());
            playContext.getResult().getCurrentTurn().setTempManaUsed(tempManaUsed);
        }
        return true;
    }
}