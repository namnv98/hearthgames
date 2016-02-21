package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.CardDetails;
import com.hearthgames.server.game.play.GameResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardSummaryAnalyzer implements Analyzer<GenericTable> {

    @Override
    public GenericTable analyze(GameResult result, GameState gameState, RawGameData rawGameData) {

        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        header.addColumn(new GenericColumn(""));
        header.addColumn(new GenericColumn("Starting Hand"));
        header.addColumn(new GenericColumn("Mulliganed"));
        header.addColumn(new GenericColumn("Cards In Deck"));
        table.setHeader(header);

        GenericRow friendly = new GenericRow();
        friendly.addColumn(new GenericColumn(gameState.getFriendlyPlayer().getName()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyStartingCards()));
        friendly.addColumn(new GenericColumn<>(result.getFriendlyMulliganedCards()));

        if (CollectionUtils.isEmpty(rawGameData.getArenaDeckCards())) {
            friendly.addColumn(new GenericColumn<>(result.getFriendlyDeckCards()));
        } else {
            List<Card> cards = new ArrayList<>();
            for (String cardId: rawGameData.getArenaDeckCards()) {
                Card card = new Card();
                card.setCardid(cardId);
                CardDetails cardDetails = new CardDetails();
                cardDetails.setId(cardId);
                card.setCardDetails(cardDetails);
                cards.add(card);
            }
            friendly.addColumn(new GenericColumn<>(cards));
        }
        table.setFriendly(friendly);

        GenericRow opposing = new GenericRow();
        opposing.addColumn(new GenericColumn(gameState.getOpposingPlayer().getName()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingStartingCards()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingMulliganedCards()));
        opposing.addColumn(new GenericColumn<>(result.getOpposingDeckCards()));
        table.setOpposing(opposing);

        return table;
    }
}
