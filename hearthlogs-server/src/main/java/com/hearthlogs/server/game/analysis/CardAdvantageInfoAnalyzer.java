package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.CardAdvantageInfo;
import com.hearthlogs.server.game.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Board;
import com.hearthlogs.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardAdvantageInfoAnalyzer extends PagingAbstractAnalyzer<CardAdvantageInfo> {

    @Override
    protected CardAdvantageInfo getInfo(GameResult result, GameContext context, List<Turn> turns) {

        CardAdvantageInfo info = new CardAdvantageInfo();
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

            int friendlyCards = board.getFriendlyHand().size() + board.getFriendlyPlay().size()
                    + board.getFriendlyWeapon().size() + board.getFriendlySecret().size() -
                    (hasTheCoin(board.getFriendlyHand()) ? 1 : 0);
            int opposingCards = board.getOpposingHand().size() + board.getOpposingPlay().size()
                    + board.getOpposingWeapon().size() + board.getOpposingSecret().size() -
                    (hasTheCoin(board.getOpposingHand()) ? 1 : 0);

            if (friendlyCards - opposingCards > 0) {
                int cardAdvantage = friendlyCards - opposingCards;
                addFriendlyOpposingColumns(""+cardAdvantage, "", friendly, opposing);
            } else if (opposingCards - friendlyCards > 0) {
                int cardAdvantage = opposingCards - friendlyCards;
                addFriendlyOpposingColumns("", ""+cardAdvantage, friendly, opposing);
            } else {
                addFriendlyOpposingColumns("", "", friendly, opposing);
            }
        }

        return info;
    }

    private void addFriendlyOpposingColumns(String friendlyData, String opposingData, GenericRow friendly, GenericRow opposing) {
        friendly.addColumn(new GenericColumn(friendlyData));
        opposing.addColumn(new GenericColumn(opposingData));
    }

    private boolean hasTheCoin(List<Card> cards) {
        for (Card card: cards) {
            if (Card.THE_COIN.equals(card.getCardid())) {
                return true;
            }
        }
        return false;
    }
}
