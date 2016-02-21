package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Action;
import com.hearthgames.server.game.play.domain.Kill;
import com.hearthgames.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

@Component
public class TradeAnalyzer implements Analyzer<GenericTable> {

    @Override
    public GenericTable analyze(GameResult result, GameState gameState, RawGameData rawGameData) {
        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        table.setHeader(header);
        header.addColumn(new GenericColumn(""));
        header.addColumn(new GenericColumn("Favorable Trades"));
        header.addColumn(new GenericColumn("Even Trades"));
        header.addColumn(new GenericColumn("Poor Trades"));

        int friendlyFavorableTrades = 0;
        int friendlyEvenTrades = 0;
        int friendlyPoorTrades = 0;
        int opposingFavorableTrades = 0;
        int opposingEvenTrades = 0;
        int opposingPoorTrades = 0;

        for (Turn turn: result.getTurns()) {
            for (Action action: turn.getActions()) {
                if (action instanceof Kill) {
                    Kill kill = (Kill) action;
                    if (kill.isFavorableTrade()) {
                        if (gameState.isFriendly(kill.getKillerController())) {
                            friendlyFavorableTrades++;
                        } else {
                            opposingFavorableTrades++;
                        }
                    } else if (kill.isEvenTrade()) {
                        if (gameState.isFriendly(kill.getKillerController())) {
                            friendlyEvenTrades++;
                        } else {
                            opposingEvenTrades++;
                        }
                    } else {
                        if (gameState.isFriendly(kill.getKillerController())) {
                            friendlyPoorTrades++;
                        } else {
                            opposingPoorTrades++;
                        }
                    }
                }
            }
        }
        GenericRow friendly = new GenericRow();
        table.setFriendly(friendly);
        friendly.addColumn(new GenericColumn(gameState.getFriendlyPlayer().getName()));
        friendly.addColumn(new GenericColumn(""+friendlyFavorableTrades));
        friendly.addColumn(new GenericColumn(""+friendlyEvenTrades));
        friendly.addColumn(new GenericColumn(""+friendlyPoorTrades));

        GenericRow opposing = new GenericRow();
        table.setOpposing(opposing);
        opposing.addColumn(new GenericColumn(gameState.getOpposingPlayer().getName()));
        opposing.addColumn(new GenericColumn(""+opposingFavorableTrades));
        opposing.addColumn(new GenericColumn(""+opposingEvenTrades));
        opposing.addColumn(new GenericColumn(""+opposingPoorTrades));

        return table;
    }
}
