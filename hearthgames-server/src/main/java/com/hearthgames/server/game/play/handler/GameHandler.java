package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.GameContext;
import org.springframework.stereotype.Component;

import static com.hearthgames.server.game.play.handler.HandlerConstants.TRUE_OR_ONE;

@Component
public class GameHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() &&
               gameContext.getActivity().isGame();
    }

    public boolean handle(GameContext gameContext) {
        GameEntity before = gameContext.getGameState().getGameEntity();
        GameEntity after = (GameEntity) gameContext.getActivity().getDelta();

        if ((before.getStep() == null && GameEntity.Step.BEGIN_MULLIGAN.eq(after.getStep())) ||
            (before.getStep() == null && GameEntity.Step.MAIN_READY.eq(after.getStep()))) {
            Card friendlyHeroCard = gameContext.getGameState().getEntityById(gameContext.getGameState().getFriendlyPlayer().getHeroEntity());
            Card opposingHeroCard = gameContext.getGameState().getEntityById(gameContext.getGameState().getOpposingPlayer().getHeroEntity());
            CardDetails friendlyCardDetails = friendlyHeroCard.getCardDetails();
            CardDetails opposingCardDetails = opposingHeroCard.getCardDetails();
            gameContext.getGameState().getFriendlyPlayer().setPlayerClass(friendlyCardDetails.getPlayerClass());
            gameContext.getGameState().getOpposingPlayer().setPlayerClass(opposingCardDetails.getPlayerClass());
            gameContext.getGameState().getFriendlyPlayer().setHeroCard(friendlyHeroCard);
            gameContext.getGameState().getOpposingPlayer().setHeroCard(opposingHeroCard);

            gameContext.addLoggingAction("The Game has started");
            gameContext.getGameState().getCards().values().stream().filter(c -> Zone.HAND.eq(c.getZone())).filter(c -> gameContext.getGameState().getStartingCardIds().contains(c.getEntityId())).forEach(c -> {
                Player player = c.getController().equals(gameContext.getGameState().getFriendlyPlayer().getController()) ? gameContext.getGameState().getFriendlyPlayer() : gameContext.getGameState().getOpposingPlayer();
                if (player == gameContext.getGameState().getFriendlyPlayer()) {
                    gameContext.getResult().addFriendlyStartingCard(c);
                } else {
                    gameContext.getResult().addOpposingStartingCard(c);
                }
                gameContext.addCardDrawn(player, c, player);
            });
            if (GameEntity.Step.BEGIN_MULLIGAN.eq(after.getStep())) {
                gameContext.addLoggingAction("Mulligan Phase has started");
            }
        }
        if (GameEntity.Step.BEGIN_MULLIGAN.eq(before.getStep()) && GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            gameContext.addLoggingAction("Mulligan Phase has ended");
            gameContext.addEndofTurn();
            gameContext.addFirstBoard();
            gameContext.getResult().setTurnNumber(1);
            gameContext.getResult().addTurn();
        } else if (before.getStep() == null && GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            // this happens for games with no mulligan phases such as Adventure mode games
            gameContext.addEndofTurn();
            gameContext.addFirstBoard();
            gameContext.getResult().setTurnNumber(1);
            gameContext.getResult().addTurn();
        }

        if (GameEntity.Step.MAIN_READY.eq(after.getStep())) {
            if (gameContext.getResult().getTurnNumber() > 1) {
                gameContext.getResult().addTurn();
            }
            gameContext.addLoggingAction("Turn " + gameContext.getResult().getTurnNumber());
            if (TRUE_OR_ONE.equals(gameContext.getGameState().getFriendlyPlayer().getCurrentPlayer())) {
                gameContext.getResult().getCurrentTurn().setWhoseTurn(gameContext.getGameState().getFriendlyPlayer());
            } else if (TRUE_OR_ONE.equals(gameContext.getGameState().getOpposingPlayer().getCurrentPlayer())) {
                gameContext.getResult().getCurrentTurn().setWhoseTurn(gameContext.getGameState().getOpposingPlayer());
            }
            gameContext.getResult().getCurrentTurn().getWhoseTurn().setNumOptions(null); // this needs resetting at the start of a turn.
            gameContext.getResult().getCurrentTurn().setStartDateTime(gameContext.getActivity().getDateTime());
        } else if (GameEntity.Step.MAIN_NEXT.eq(after.getStep()) || GameEntity.Step.FINAL_GAMEOVER.eq(after.getStep())) {
            gameContext.getResult().getCurrentTurn().setEndDateTime(gameContext.getActivity().getDateTime());
        }

        if (GameEntity.Step.MAIN_START.eq(after.getStep()) && gameContext.getResult().getCurrentTurn().getManaGained() == 0) {

            // After 10 of one players turns the game automatically gives you 10 mana without recording it in the log.
            // I check here if the mana gained was still 0 after the MAIN_READY finishes, if so we set it to 10.
            gameContext.addManaGained(gameContext.getResult().getCurrentTurn().getWhoseTurn(), 10);
        }

        if (after.getTurn() != null && gameContext.getGameState().getGameEntity().isGameRunning()) {
            int turnNumber = Integer.parseInt(after.getTurn());
            gameContext.getResult().setTurnNumber(turnNumber);
        }

        if (GameEntity.State.COMPLETE.eq(after.getState())) {
            gameContext.addLoggingAction("Game Over");
        }

        return true;
    }

}
