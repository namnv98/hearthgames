package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.TurnInfo;
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
        turn.getActions().stream().filter(action -> action instanceof Board).forEach(action -> boards.add((Board) action));
        info.setBoards(boards);

        return info;
    }

}
