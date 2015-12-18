package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.domain.board.Board;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.game.play.domain.board.CardInHand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardAdvantageAnalyzer extends PagingAbstractAnalyzer<GenericTable> {

    @Override
    protected GenericTable getInfo(GameResult result, GameContext context, List<Turn> turns) {

        GenericTable table = new GenericTable();
        GenericRow header = new GenericRow();
        table.setHeader(header);
        header.addColumn(new GenericColumn(""));
        for (Turn turn: turns) {
            header.addColumn(new GenericColumn(""+turn.getTurnNumber()));
        }

        GenericRow friendly = new GenericRow();
        table.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));

        GenericRow opposing = new GenericRow();
        table.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));

        for (Turn turn: turns) {

            Board board = turn.findLastBoard();
            if (board != null) {
                int weapon = board.getFriendlyHero().getWeapon() != null ? 1 : 0;
                int friendlyCards = board.getFriendlyHero().getCardsInHand().size() + board.getFriendlyHero().getMinionsInPlay().size()
                        + weapon + board.getFriendlyHero().getCardsInSecret().size();
                weapon = board.getOpposingHero().getWeapon() != null ? 1 : 0;
                int opposingCards = board.getOpposingHero().getCardsInHand().size() + board.getOpposingHero().getMinionsInPlay().size()
                        + weapon + board.getOpposingHero().getCardsInSecret().size();

                if (friendlyCards - opposingCards > 0) {
                    int cardAdvantage = friendlyCards - opposingCards;
                    addFriendlyOpposingColumns(""+cardAdvantage, "", friendly, opposing);
                } else if (opposingCards - friendlyCards > 0) {
                    int cardAdvantage = opposingCards - friendlyCards;
                    addFriendlyOpposingColumns("", ""+cardAdvantage, friendly, opposing);
                } else {
                    addFriendlyOpposingColumns("", "", friendly, opposing);
                }
            } else {
                addFriendlyOpposingColumns("", "", friendly, opposing);
            }
        }

        return table;
    }

    private void addFriendlyOpposingColumns(String friendlyData, String opposingData, GenericRow friendly, GenericRow opposing) {
        friendly.addColumn(new GenericColumn(friendlyData));
        opposing.addColumn(new GenericColumn(opposingData));
    }
}
