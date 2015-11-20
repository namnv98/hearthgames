package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.TurnInfo;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Board;
import com.hearthlogs.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

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

        info.setTurnNumber(""+turn.getTurnNumber());
        info.setWhoseTurn(turn.getWhoseTurn().getName());
        if (turn.getWhoseTurn() == context.getFriendlyPlayer()) {
            String friendlyClass = result.getWinner() == result.getFriendly() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(friendlyClass);
        } else {
            String opposingClass = result.getWinner() == result.getOpposing() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(opposingClass);
        }
        Board board = turn.findFirstBoard();

        info.setFriendlyHand(board.getFriendlyHand());
        info.setFriendlyPlay(board.getFriendlyPlay());
        info.setFriendlySecret(board.getFriendlySecret());
        info.setFriendlyWeapon(board.getFriendlyWeapon());

        info.setOpposingHand(board.getOpposingHand());
        info.setOpposingPlay(board.getOpposingPlay());
        info.setOpposingSecret(board.getOpposingSecret());
        info.setOpposingWeapon(board.getOpposingWeapon());

        return info;
    }

}
