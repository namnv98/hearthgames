package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.TurnColumn;
import com.hearthlogs.server.game.analysis.domain.TurnInfo;
import com.hearthlogs.server.game.analysis.domain.TurnRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.*;
import com.hearthlogs.server.game.play.domain.board.Board;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TurnInfoAnalyzer implements Analyzer<List<TurnInfo>> {

    @Override
    public List<TurnInfo> analyze(GameResult result, GameContext context) {
        return result.getTurns().stream().map(turn -> getTurnInfo(result, context, turn)).collect(Collectors.toList());
    }

    private TurnInfo getTurnInfo(GameResult result, GameContext context, Turn turn) {
        TurnInfo info = new TurnInfo();

        Player whoseTurn = turn.getWhoseTurn();
        if (whoseTurn == null) {
            whoseTurn = "1".equals(context.getFriendlyPlayer().getFirstPlayer()) ? context.getFriendlyPlayer() : context.getOpposingPlayer();
        }
        info.setTurnNumber(""+turn.getTurnNumber());
        info.setWhoseTurn(whoseTurn.getName());
        if (whoseTurn == context.getFriendlyPlayer()) {
            String friendlyClass = result.getWinner() == context.getFriendlyPlayer() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(friendlyClass);
        } else {
            String opposingClass = result.getWinner() == context.getOpposingPlayer() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(opposingClass);
        }

        List<Board> boards = new ArrayList<>();
        List<Action> actionsForBoard = new ArrayList<>();
        for (Action action: turn.getActions()) {
            if (isRegularAction(action)) {
                actionsForBoard.add(action);
            } else if (action instanceof Board) {
                Board board = (Board) action;
                actionsForBoard.forEach(board::addAction);
                actionsForBoard = new ArrayList<>();
                boards.add(board);
            }
        }
        info.setBoards(boards);

        return info;
    }

    private boolean isRegularAction(Action action) {
        return action instanceof ArmorChange || action instanceof AttackChange || action instanceof CardCreation ||
               action instanceof CardDrawn || action instanceof CardPlayed || action instanceof Damage ||
               action instanceof Frozen || action instanceof HealthChange || action instanceof HeroHealthChange ||
               action instanceof HeroPowerUsed || action instanceof Joust || action instanceof Kill ||
               action instanceof ManaGained || action instanceof ManaUsed || action instanceof TempManaGained ||
               action instanceof Trigger || action instanceof CardDiscarded;
    }

}
