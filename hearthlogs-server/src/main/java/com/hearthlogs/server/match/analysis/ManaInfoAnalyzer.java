package com.hearthlogs.server.match.analysis;

import com.hearthlogs.server.match.analysis.domain.ManaInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.Turn;
import org.springframework.stereotype.Component;

import java.util.IntSummaryStatistics;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Component
public class ManaInfoAnalyzer implements Analyzer<ManaInfo> {

    @Override
    public ManaInfo analyze(MatchResult result, ParseContext context) {
        ManaInfo manaInfo = new ManaInfo();

        int manaNotNeeded = result.getCurrentTurn().getManaGained() - result.getCurrentTurn().getManaUsed();
        if (result.getCurrentTurn().getWhoseTurn() == context.getFriendlyPlayer()) {
            manaInfo.setFriendlyNotNeeded(manaNotNeeded);
        } else {
            manaInfo.setOpposingNotNeeded(manaNotNeeded);
        }

        manaInfo.setFriendlyTotalMana(calcFriendlyTotalMana(result.getTurns(), result.getFriendly()));
        manaInfo.setFriendlyManaUsed(calcFriendlyManaUsed(result.getTurns(), result.getFriendly()));
        manaInfo.setFriendlyManaSaved(calcFriendlyManaSaved(result.getTurns(), result.getFriendly()));
        manaInfo.setFriendlyManaLost(calcFriendlyManaLost(result.getTurns(), result.getFriendly()));
        manaInfo.setFriendlyManaEfficiency(
            calcFriendlyManaEfficiency(manaInfo.getFriendlyManaUsed(), manaInfo.getFriendlyTotalMana(), manaInfo.getFriendlyManaSaved(), manaInfo.getFriendlyNotNeeded())
        );

        manaInfo.setOpposingTotalMana(calcOpposingTotalMana(result.getTurns(), result.getOpposing()));
        manaInfo.setOpposingManaUsed(calcOpposingManaUsed(result.getTurns(), result.getOpposing()));
        manaInfo.setOpposingManaSaved(calcOpposingManaSaved(result.getTurns(), result.getOpposing()));
        manaInfo.setOpposingManaLost(calcOpposingManaLost(result.getTurns(), result.getOpposing()));
        manaInfo.setOpposingManaEfficiency(
            calcOpposingManaEfficiency(manaInfo.getOpposingManaUsed(), manaInfo.getOpposingTotalMana(), manaInfo.getOpposingManaSaved(), manaInfo.getOpposingNotNeeded())
        );


        return manaInfo;
    }

    private long calcFriendlyTotalMana(Set<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaGained);
    }

    private long calcFriendlyManaUsed(Set<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaUsed);
    }

    private long calcFriendlyManaSaved(Set<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaSaved);
    }

    private long calcFriendlyManaLost(Set<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaLost);
    }

    private float calcFriendlyManaEfficiency(long friendlyManaUsed, long friendlyTotalMana, long friendlyManaSaved, long friendlyManaNotNeeded) {
        return (float) friendlyManaUsed / (friendlyTotalMana - friendlyManaSaved - friendlyManaNotNeeded);
    }

    private long calcOpposingTotalMana(Set<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaGained);
    }

    private long calcOpposingManaUsed(Set<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaUsed);
    }

    private long calcOpposingManaSaved(Set<Turn> turns, Player opposing) {
        return getMana(turns, opposing, Turn::getManaSaved);
    }

    private long calcOpposingManaLost(Set<Turn> turns, Player friendly) {
        return getMana(turns, friendly, Turn::getManaLost);
    }

    private long getMana(Set<Turn> turns, Player player, ToIntFunction<Turn> turnToIntFunction) {
        IntSummaryStatistics stats = turns.stream().filter(t -> t.getWhoseTurn() == player).collect(Collectors.summarizingInt(turnToIntFunction));
        return stats.getSum();
    }

    private float calcOpposingManaEfficiency(long opposingManaUsed, long opposingTotalMana, long opposingManaSaved, long opposingManaNotNeeded) {
        return (float) opposingManaUsed / (opposingTotalMana - opposingManaSaved - opposingManaNotNeeded);
    }

}
