package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.game.play.domain.board.Board;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TurnInfoAnalyzer implements Analyzer<List<TurnInfo>> {

    @Override
    public List<TurnInfo> analyze(GameResult result, GameState gameState, RawGameData rawGameData) {
        return result.getTurns().stream().map(turn -> getTurnInfo(result, gameState, turn)).collect(Collectors.toList());
    }

    private TurnInfo getTurnInfo(GameResult result, GameState gameState, Turn turn) {
        TurnInfo info = new TurnInfo();

        Player whoseTurn = turn.getWhoseTurn();
        if (whoseTurn == null) {
            whoseTurn = "1".equals(gameState.getFriendlyPlayer().getFirstPlayer()) ? gameState.getFriendlyPlayer() : gameState.getOpposingPlayer();
        }
        info.setTurnNumber(""+turn.getTurnNumber());
        info.setWhoseTurn(whoseTurn.getName());
        if (whoseTurn == gameState.getFriendlyPlayer()) {
            String friendlyClass = result.getWinner().equals(gameState.getFriendlyPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(friendlyClass);
        } else {
            String opposingClass = result.getWinner().equals(gameState.getOpposingPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(opposingClass);
        }

        List<Board> boards = new ArrayList<>();
        turn.getActions().stream().filter(action -> action instanceof Board).forEach(action -> boards.add((Board) action));
        info.setBoards(boards);

        return info;
    }

}
