package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.domain.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof GameEntity;
    }

    public boolean handle(GameResult result, GameContext context, Activity activity) {
        GameEntity before = context.getGameEntity();
        GameEntity after = (GameEntity) activity.getDelta();

        if (before.getStep() == null && GameEntity.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
            System.out.println("---------------------  The Game has started  ----------------------------------");
            context.getCards().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> context.getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                Player player = c.getController().equals(context.getFriendlyPlayer().getController()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();
                if (player == context.getFriendlyPlayer()) {
                    result.addFriendlyStartingCard(c);
                } else {
                    result.addOpposingStartingCard(c);
                }
                System.out.println(player.getName() + " has drawn " + c.getName() + " (id=" + c.getEntityId()+ ")");
            });


            System.out.println("--------------------  Mulligan Phase has started  -----------------------------");
        }
        if (GameEntity.Step.BEGIN_MULLIGAN.eq(before.getStep()) && GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println("--------------------  Mulligan Phase has ended  -------------------------------");
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + context.getGame().getTurn() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        if (GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TURN " + result.getTurnNumber() + "  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            if (result.getTurnNumber() > 1) {
                result.addTurn();
            }
            if (TRUE_OR_ONE.equals(context.getFriendlyPlayer().getCurrentPlayer())) {
                result.getCurrentTurn().setWhoseTurn(context.getFriendlyPlayer());
            } else if (TRUE_OR_ONE.equals(context.getOpposingPlayer().getCurrentPlayer())) {
                result.getCurrentTurn().setWhoseTurn(context.getOpposingPlayer());
            }

            result.getCurrentTurn().setStartDateTime(activity.getDateTime());
        } else if (GameEntity.Step.MAIN_NEXT.eq(after.getStep()) || GameEntity.Step.FINAL_GAMEOVER.eq(after.getStep())) {
            result.getCurrentTurn().setEndDateTime(activity.getDateTime());
        }

        if (GameEntity.Step.MAIN_START.eq(after.getStep())) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            if (result.getCurrentTurn().getManaGained() == 0) {
                result.addManaGained(10);
            }
        }

        if (after.getTurn() != null && context.getGameEntity().isGameRunning()) {
            int turnNumber = Integer.parseInt(after.getTurn());
            result.setTurnNumber(turnNumber);
        }

        if (GameEntity.State.COMPLETE.eq(after.getState())) {
            Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());

            CardDetails friendlyCardDetails = friendlyHeroCard.getCardDetails();
            CardDetails opposingCardDetails = opposingHeroCard.getCardDetails();

            if (result.getWinner() == context.getFriendlyPlayer()) {
                result.setWinnerClass(friendlyCardDetails.getPlayerClass());
                result.setLoserClass(opposingCardDetails.getPlayerClass());
            } else {
                result.setWinnerClass(opposingCardDetails.getPlayerClass());
                result.setLoserClass(friendlyCardDetails.getPlayerClass());
            }

            context.getFriendlyPlayer().setPlayerClass(friendlyCardDetails.getPlayerClass());
            context.getOpposingPlayer().setPlayerClass(opposingCardDetails.getPlayerClass());
            context.getFriendlyPlayer().setHeroCard(friendlyHeroCard);
            context.getOpposingPlayer().setHeroCard(opposingHeroCard);

            System.out.println("--------------------------  Game Over  ----------------------------------------");
        }

        return true;
    }

}
