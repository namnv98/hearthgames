package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.play.GameResult;
import org.springframework.stereotype.Component;

@Component
public class CardSummaryAnalyzer implements Analyzer<GenericTable> {

    @Override
    public GenericTable analyze(GameResult result, GameContext context) {

        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        header.addColumn(new GenericColumn(""));
        header.addColumn(new GenericColumn("Starting Hand"));
        header.addColumn(new GenericColumn("Mulliganed Cards"));
        header.addColumn(new GenericColumn("Cards In Deck"));
        table.setHeader(header);

        GenericRow friendly = new GenericRow();
        friendly.addColumn(new GenericColumn(context.getFriendlyPlayer().getName()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyStartingCards()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyMulliganedCards()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyDeckCards()));
        table.setFriendly(friendly);

        GenericRow opposing = new GenericRow();
        opposing.addColumn(new GenericColumn(context.getOpposingPlayer().getName()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingStartingCards()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingMulliganedCards()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingDeckCards()));
        table.setOpposing(opposing);

        return table;
    }
}
