package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.game.play.domain.board.Board;
import com.hearthgames.server.game.play.domain.board.MinionInPlay;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardControlAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    @Override
    protected GenericTable getInfo(GameResult result, GameState gameState, RawGameData rawGameData, List<Turn> turns) {

        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        table.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        table.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(gameState.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        table.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(gameState.getOpposingPlayer().getName()));


        for (Turn turn: turns) {

            Board board = turn.findLastBoard();
            if (board != null) {
                boolean friendlyBoardControl = false;
                boolean opposingBoardControl = false;
                if (board.getFriendlyHero().getMinionsInPlay().size() != board.getOpposingHero().getMinionsInPlay().size()) {
                    int friendlyAttackTotal = 0;
                    int friendlyHealthTotal = 0;
                    for (MinionInPlay minion : board.getFriendlyHero().getMinionsInPlay()) {
                        friendlyAttackTotal += minion.getAttack();
                        friendlyHealthTotal += minion.getHealth();
                    }

                    int opposingAttackTotal = 0;
                    int opposingHealthTotal = 0;
                    for (MinionInPlay minion : board.getOpposingHero().getMinionsInPlay()) {
                        opposingAttackTotal += minion.getAttack();
                        opposingHealthTotal += minion.getHealth();
                    }

                    if (friendlyAttackTotal > opposingHealthTotal && friendlyHealthTotal > opposingAttackTotal) {
                        friendlyBoardControl = true;
                    } else if (opposingAttackTotal > friendlyHealthTotal && opposingHealthTotal > friendlyAttackTotal) {
                        opposingBoardControl = true;
                    }
                }

                String friendlyClass = result.getWinner().equals(gameState.getFriendlyPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();
                String opposingClass = result.getWinner().equals(gameState.getOpposingPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();

                if (friendlyBoardControl) {
                    addFriendlyOpposingColumns(friendlyClass, "", friendly, opposing);
                } else if (opposingBoardControl) {
                    addFriendlyOpposingColumns("", opposingClass, friendly, opposing);
                } else {
                    addFriendlyOpposingColumns("", "", friendly, opposing);
                }
            } else {
                addFriendlyOpposingColumns("", "", friendly, opposing);
            }
        }

        return table;
    }

    private void addFriendlyOpposingColumns(String friendlyClass, String opposingClass, GenericRow friendly, GenericRow opposing) {
        friendly.addColumn(new GenericColumn(friendlyClass));
        opposing.addColumn(new GenericColumn(opposingClass));
    }
}