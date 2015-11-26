package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.TurnColumn;
import com.hearthlogs.server.game.analysis.domain.TurnInfo;
import com.hearthlogs.server.game.analysis.domain.TurnRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.*;
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
            whoseTurn = "1".equals(result.getFriendly().getFirstPlayer()) ? result.getFriendly() : result.getOpposing();
        }
        info.setTurnNumber(""+turn.getTurnNumber());
        info.setWhoseTurn(whoseTurn.getName());
        if (whoseTurn == context.getFriendlyPlayer()) {
            String friendlyClass = result.getWinner() == result.getFriendly() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(friendlyClass);
        } else {
            String opposingClass = result.getWinner() == result.getOpposing() ? result.getWinnerClass() : result.getLoserClass();
            info.setTurnClass(opposingClass);
        }

        List<TurnRow> rows = new ArrayList<>();

        for (Action action: turn.getActions()) {
            if (isRegularAction(action)) {
                TurnRow row = new TurnRow(1);
                row.addColumn(new TurnColumn(action));
                rows.add(row);

            } else if (action instanceof Board) {
                TurnRow row = new TurnRow(2);
                row.addColumn(new TurnColumn(""));
                row.addColumn(new TurnColumn("Hand"));
                row.addColumn(new TurnColumn("Weapon"));
                row.addColumn(new TurnColumn("Secrets"));
                row.addColumn(new TurnColumn("Minions In Play"));
                rows.add(row);

                Board board = (Board) action;
                row = new TurnRow(3);
                row.addColumn(new TurnColumn(context.getFriendlyPlayer().getName()));
                row.addColumn(new TurnColumn(board.getFriendlyHand()));
                row.addColumn(new TurnColumn(board.getFriendlyWeapon()));
                row.addColumn(new TurnColumn(board.getFriendlySecret()));
                row.addColumn(new TurnColumn(board.getFriendlyPlay()));
                rows.add(row);

                row = new TurnRow(4);
                row.addColumn(new TurnColumn(context.getOpposingPlayer().getName()));
                row.addColumn(new TurnColumn(board.getOpposingHand()));
                row.addColumn(new TurnColumn(board.getOpposingWeapon()));
                row.addColumn(new TurnColumn(board.getOpposingSecret()));
                row.addColumn(new TurnColumn(board.getOpposingPlay()));
                rows.add(row);
            }
        }
        info.setRows(rows);

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
