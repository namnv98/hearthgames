package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Board;
import com.hearthlogs.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardControlAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    @Override
    protected GenericTable getInfo(GameResult result, GameContext context, List<Turn> turns) {

        GenericTable info = new GenericTable();

        GenericRow header = new GenericRow();
        info.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        info.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        info.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));


        for (Turn turn: turns) {

            Board board = turn.findLastBoard();

            boolean friendlyBoardControl = false;
            boolean opposingBoardControl = false;
            if (board.getFriendlyPlay().size() != board.getOpposingPlay().size()) {
                int friendlyAttackTotal = 0;
                int friendlyHealthTotal = 0;
                for (Card c: board.getFriendlyPlay()) {
                    if (c.getAtk() != null) {
                        friendlyAttackTotal += Integer.parseInt(c.getAtk());
                    }
                    if (c.getHealth() != null) {
                        friendlyHealthTotal += Integer.parseInt(c.getHealth());
                    }
                }

                int opposingAttackTotal = 0;
                int opposingHealthTotal = 0;
                for (Card c: board.getOpposingPlay()) {
                    if (c.getAtk() != null) {
                        opposingAttackTotal += Integer.parseInt(c.getAtk());
                    }
                    if (c.getHealth() != null) {
                        opposingHealthTotal += Integer.parseInt(c.getHealth());
                    }
                }

                if (friendlyAttackTotal > opposingHealthTotal && friendlyHealthTotal > opposingAttackTotal) {
                    friendlyBoardControl = true;
                } else if (opposingAttackTotal > friendlyHealthTotal && opposingHealthTotal > friendlyAttackTotal) {
                    opposingBoardControl = true;
                }
            }

            String friendlyClass = result.getWinner() == result.getFriendly() ? result.getWinnerClass() : result.getLoserClass();
            String opposingClass = result.getWinner() == result.getOpposing() ? result.getWinnerClass() : result.getLoserClass();

            if (friendlyBoardControl) {
                addFriendlyOpposingColumns(friendlyClass, "", friendly, opposing);
            } else if (opposingBoardControl) {
                addFriendlyOpposingColumns("", opposingClass, friendly, opposing);
            } else {
                addFriendlyOpposingColumns("", "", friendly, opposing);
            }
        }

        return info;
    }

    private void addFriendlyOpposingColumns(String friendlyClass, String opposingClass, GenericRow friendly, GenericRow opposing) {
        friendly.addColumn(new GenericColumn(friendlyClass));
        opposing.addColumn(new GenericColumn(opposingClass));
    }
}