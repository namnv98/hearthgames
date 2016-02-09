package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.PlayContext;
import org.springframework.stereotype.Component;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

@Component
public class GameHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() &&
               playContext.getActivity().isGame();
    }

    public boolean handle(PlayContext playContext) {
        GameEntity before = playContext.getContext().getGameEntity();
        GameEntity after = (GameEntity) playContext.getActivity().getDelta();

        if ((before.getStep() == null && GameEntity.Step.BEGIN_MULLIGAN.eq(after.getStep())) ||
            (before.getStep() == null && GameEntity.Step.MAIN_READY.eq(after.getStep()))) {
            Card friendlyHeroCard = playContext.getContext().getEntityById(playContext.getContext().getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = playContext.getContext().getEntityById(playContext.getContext().getOpposingPlayer().getHeroEntity());
            CardDetails friendlyCardDetails = friendlyHeroCard.getCardDetails();
            CardDetails opposingCardDetails = opposingHeroCard.getCardDetails();
            playContext.getContext().getFriendlyPlayer().setPlayerClass(friendlyCardDetails.getPlayerClass());
            playContext.getContext().getOpposingPlayer().setPlayerClass(opposingCardDetails.getPlayerClass());
            playContext.getContext().getFriendlyPlayer().setHeroCard(friendlyHeroCard);
            playContext.getContext().getOpposingPlayer().setHeroCard(opposingHeroCard);

            playContext.addLoggingAction("The Game has started");
            playContext.getContext().getCards().values().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> playContext.getContext().getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                Player player = c.getController().equals(playContext.getContext().getFriendlyPlayer().getController()) ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer();
                if (player == playContext.getContext().getFriendlyPlayer()) {
                    playContext.getResult().addFriendlyStartingCard(c);
                } else {
                    playContext.getResult().addOpposingStartingCard(c);
                }
                playContext.addCardDrawn(player, c, player);
            });
            if (GameEntity.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
                playContext.addLoggingAction("Mulligan Phase has started");
            }
        }
        if (GameEntity.Step.BEGIN_MULLIGAN.eq(before.getStep()) && GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            playContext.addLoggingAction("Mulligan Phase has ended");
            playContext.addEndofTurn();
            playContext.addFirstBoard();
            playContext.getResult().setTurnNumber(1);
            playContext.getResult().addTurn();
        } else if (before.getStep() == null && GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            // this happens for games with no mulligan phases such as Adventure mode games
            playContext.addEndofTurn();
            playContext.addFirstBoard();
            playContext.getResult().setTurnNumber(1);
            playContext.getResult().addTurn();
        }

        if (GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            if (playContext.getResult().getTurnNumber() > 1) {
                playContext.getResult().addTurn();
            }
            playContext.addLoggingAction("Turn " + playContext.getResult().getTurnNumber());
            if (TRUE_OR_ONE.equals(playContext.getContext().getFriendlyPlayer().getCurrentPlayer())) {
                playContext.getResult().getCurrentTurn().setWhoseTurn(playContext.getContext().getFriendlyPlayer());
            } else if (TRUE_OR_ONE.equals(playContext.getContext().getOpposingPlayer().getCurrentPlayer())) {
                playContext.getResult().getCurrentTurn().setWhoseTurn(playContext.getContext().getOpposingPlayer());
            }
            playContext.getResult().getCurrentTurn().getWhoseTurn().setNumOptions(null); // this needs resetting at the start of a turn.
            playContext.getResult().getCurrentTurn().setStartDateTime(playContext.getActivity().getDateTime());
        } else if (GameEntity.Step.MAIN_NEXT.eq(after.getStep()) || GameEntity.Step.FINAL_GAMEOVER.eq(after.getStep())) {
            playContext.getResult().getCurrentTurn().setEndDateTime(playContext.getActivity().getDateTime());
        }

        if (GameEntity.Step.MAIN_START.eq(after.getStep()) && playContext.getResult().getCurrentTurn().getManaGained() == 0) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            playContext.addManaGained(playContext.getResult().getCurrentTurn().getWhoseTurn(), 10);
        }

        if (after.getTurn() != null && playContext.getContext().getGameEntity().isGameRunning()) {
            int turnNumber = Integer.parseInt(after.getTurn());
            playContext.getResult().setTurnNumber(turnNumber);
        }

        if (GameEntity.State.COMPLETE.eq(after.getState())) {
            playContext.addLoggingAction("Game Over");
        }

        return true;
    }

}
