package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.generic.GenericColumn;
import com.hearthgames.server.game.analysis.domain.generic.GenericRow;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Component
public class ManaSummaryAnalyzer implements Analyzer<GenericTable> {

    private static final NumberFormat numberFormat = NumberFormat.getPercentInstance();

    static {
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public GenericTable analyze(GameResult result, GameState gameState, RawGameData rawGameData) {
        GenericTable table = new GenericTable();

        GenericRow header = new GenericRow();
        header.addColumn(new GenericColumn(""));
        header.addColumn(new GenericColumn("Used"));
        header.addColumn(new GenericColumn("Gained"));
        header.addColumn(new GenericColumn("Lost"));
        header.addColumn(new GenericColumn("Saved"));
        header.addColumn(new GenericColumn("Not Needed"));
        header.addColumn(new GenericColumn("Efficiency"));
        table.setHeader(header);

        int friendlyManaNotNeeded = 0;
        int opposingManaNotNeeded = 0;
        int manaNotNeeded = result.getCurrentTurn().getManaGained() - result.getCurrentTurn().getManaUsed();
        if (result.getCurrentTurn().getWhoseTurn() == gameState.getFriendlyPlayer()) {
            friendlyManaNotNeeded = manaNotNeeded;
        } else {
            opposingManaNotNeeded = manaNotNeeded;
        }

        GenericRow friendly = new GenericRow();
        long friendlyManaUsed = calcFriendlyManaUsed(result.getTurns(), gameState.getFriendlyPlayer());
        long friendlyManaGained = calcFriendlyManaGained(result.getTurns(), gameState.getFriendlyPlayer());
        long friendlyManaLost = calcFriendlyManaLost(result.getTurns(), gameState.getFriendlyPlayer());
        long friendlyManaSaved = calcFriendlyManaSaved(result.getTurns(), gameState.getFriendlyPlayer());
        friendly.addColumn(new GenericColumn(gameState.getFriendlyPlayer().getName()));
        friendly.addColumn(new GenericColumn(""+friendlyManaUsed));
        friendly.addColumn(new GenericColumn(""+friendlyManaGained));
        friendly.addColumn(new GenericColumn(""+friendlyManaLost));
        friendly.addColumn(new GenericColumn(""+friendlyManaSaved));
        friendly.addColumn(new GenericColumn(""+friendlyManaNotNeeded));
        friendly.addColumn(new GenericColumn(""+
            numberFormat.format(calcFriendlyManaEfficiency(friendlyManaUsed, friendlyManaGained, friendlyManaSaved, friendlyManaNotNeeded)))
        );
        table.setFriendly(friendly);

        GenericRow opposing = new GenericRow();
        long opposingManaUsed = calcOpposingManaUsed(result.getTurns(), gameState.getOpposingPlayer());
        long opposingManaGained = calcOpposingManaGained(result.getTurns(), gameState.getOpposingPlayer());
        long opposingManaLost = calcOpposingManaLost(result.getTurns(), gameState.getOpposingPlayer());
        long opposingManaSaved = calcOpposingManaSaved(result.getTurns(), gameState.getOpposingPlayer());
        opposing.addColumn(new GenericColumn(gameState.getOpposingPlayer().getName()));
        opposing.addColumn(new GenericColumn(""+opposingManaUsed));
        opposing.addColumn(new GenericColumn(""+opposingManaGained));
        opposing.addColumn(new GenericColumn(""+opposingManaLost));
        opposing.addColumn(new GenericColumn(""+opposingManaSaved));
        opposing.addColumn(new GenericColumn(""+opposingManaNotNeeded));
        opposing.addColumn(new GenericColumn(""+
                numberFormat.format(calcOpposingManaEfficiency(opposingManaUsed, opposingManaGained, opposingManaSaved, opposingManaNotNeeded)))
        );
        table.setOpposing(opposing);

        return table;
    }

    private long calcFriendlyManaGained(List<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaGained);
    }

    private long calcFriendlyManaUsed(List<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaUsed);
    }

    private long calcFriendlyManaSaved(List<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaSaved);
    }

    private long calcFriendlyManaLost(List<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaLost);
    }

    private float calcFriendlyManaEfficiency(long friendlyManaUsed, long friendlyTotalMana, long friendlyManaSaved, long friendlyManaNotNeeded) {
        return (float) friendlyManaUsed / (friendlyTotalMana - friendlyManaSaved - friendlyManaNotNeeded);
    }

    private long calcOpposingManaGained(List<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaGained);
    }

    private long calcOpposingManaUsed(List<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaUsed);
    }

    private long calcOpposingManaSaved(List<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaSaved);
    }

    private long calcOpposingManaLost(List<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaLost);
    }

    private long getMana(List<Turn> turns, Player player, ToIntFunction<Turn> turnToIntFunction) {
        IntSummaryStatistics stats = turns.stream().filter(t -> t.getWhoseTurn() == player).collect(Collectors.summarizingInt(turnToIntFunction));
        return stats.getSum();
    }

    private float calcOpposingManaEfficiency(long opposingManaUsed, long opposingTotalMana, long opposingManaSaved, long opposingManaNotNeeded) {
        return (float) opposingManaUsed / (opposingTotalMana - opposingManaSaved - opposingManaNotNeeded);
    }

}
