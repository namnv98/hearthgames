package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.CardDetails;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameContext;
import org.springframework.stereotype.Component;

import static com.hearthgames.server.game.play.handler.HandlerConstants.FALSE_OR_ZERO;
import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

@Component
public class PlayerHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() && gameContext.getActivity().getDelta() instanceof Player;
    }

    public boolean handle(GameContext gameContext) {
        Player before = (Player) gameContext.getGameState().getEntityById(gameContext.getActivity().getEntityId());
        Player after = (Player) gameContext.getActivity().getDelta();

        if (after.getNumTurnsLeft() != null && FALSE_OR_ZERO.equals(after.getNumTurnsLeft())) {
            gameContext.addEndofTurn();
        }

        if (after.getNumOptions() != null) {
//            playContext.addNumOptions(Integer.parseInt(after.getNumOptions()));
//            playContext.addLoggingAction(playContext.getResult().getCurrentTurn().getWhoseTurn().getName() + " has " + after.getNumOptions() + " possible moves to make.");
        }

        if (after.getPlaystate() != null && (Player.PlayState.QUIT.eq(after.getPlaystate()) || Player.PlayState.CONCEDED.eq(after.getPlaystate()))) {
            gameContext.addLoggingAction(before.getName() + " has quit.");
            gameContext.getResult().setQuitter(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.WON.eq(after.getPlaystate())) {
            gameContext.addLoggingAction(before.getName() + " has won.");
            gameContext.getResult().getWinners().add(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.LOST.eq(after.getPlaystate())) {
            gameContext.addLoggingAction(before.getName() + " has lost.");
            gameContext.getResult().getLosers().add(before);
        }
        if (TRUE_OR_ONE.equals(before.getFirstPlayer())) {
            gameContext.getResult().setFirst(before);
        }

        if (after.getResources() != null) {
            gameContext.addManaGained(gameContext.getResult().getCurrentTurn().getWhoseTurn(), Integer.parseInt(after.getResources()));
        } else if (after.getResourcesUsed() != null && !HandlerConstants.FALSE_OR_ZERO.equals(after.getResourcesUsed()) && before == gameContext.getResult().getCurrentTurn().getWhoseTurn()) {
            Card usedOn = gameContext.getActivity().getParent().getDelta();

            int manaUsed = Integer.parseInt(after.getResourcesUsed()) - gameContext.getResult().getCurrentTurn().getManaUsed();
            if (gameContext.getResult().getCurrentTurn().getTempManaUsed() > 0) {
                manaUsed += gameContext.getResult().getCurrentTurn().getTempManaUsed();
                gameContext.getResult().getCurrentTurn().setTempManaUsed(0);
            }
            gameContext.addManaUsed(gameContext.getResult().getCurrentTurn().getWhoseTurn(), usedOn, manaUsed);

            if (gameContext.getActivity().getParent().getDelta().isCard()) {
                CardDetails cardDetails = usedOn.getCardDetails();
                if (cardDetails != null) {
                    int cost = cardDetails.getCost();
                    if (manaUsed < cost) {
                        gameContext.addManaSaved(usedOn, cost - manaUsed);
                    } else if (manaUsed > cost){
                        gameContext.addManaLost(usedOn, manaUsed - cost);
                    }
                }
            }
        } else if ((before.getTempResources() == null || HandlerConstants.FALSE_OR_ZERO.equals(before.getTempResources())) && after.getTempResources() != null) {
            Card fromCard = gameContext.getActivity().getParent().getDelta();
            gameContext.addTempManaGained(gameContext.getResult().getCurrentTurn().getWhoseTurn(), fromCard, Integer.parseInt(after.getTempResources()));
        } else if (before.getTempResources() != null && after.getTempResources() != null) {
            int tempManaUsed = Integer.parseInt(before.getTempResources()) - Integer.parseInt(after.getTempResources());
            gameContext.getResult().getCurrentTurn().setTempManaUsed(tempManaUsed);
        }
        return true;
    }
}