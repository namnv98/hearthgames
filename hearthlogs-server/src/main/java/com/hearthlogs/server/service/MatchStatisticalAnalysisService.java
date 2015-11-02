package com.hearthlogs.server.service;

import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.play.domain.Turn;
import com.hearthlogs.server.match.stats.domain.MatchStatistics;
import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Service
public class MatchStatisticalAnalysisService {

    public MatchStatistics calculateStatistics(MatchResult result, ParsedMatch parsedMatch) {

        MatchStatistics stats = new MatchStatistics();

        stats.setFriendlyTotalMana(calcFriendlyTotalMana(result.getTurns(), result.getFriendly()));
        stats.setFriendlyManaUsed(calcFriendlyManaUsed(result.getTurns(), result.getFriendly()));
        stats.setFriendlyManaSaved(calcFriendlyManaSaved(result.getTurns(), result.getFriendly()));
        stats.setFriendlyManaEfficiency(calcFriendlyManaEfficiency(stats.getFriendlyManaUsed(), stats.getFriendlyTotalMana(), stats.getFriendlyManaSaved()));

        stats.setOpposingTotalMana(calcOpposingTotalMana(result.getTurns(), result.getOpposing()));
        stats.setOpposingManaUsed(calcOpposingManaUsed(result.getTurns(), result.getOpposing()));
        stats.setOpposingManaSaved(calcOpposingManaSaved(result.getTurns(), result.getOpposing()));
        stats.setOpposingManaEfficiency(calcOpposingManaEfficiency(stats.getOpposingManaUsed(), stats.getOpposingTotalMana(), stats.getOpposingManaSaved()));


        return stats;
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

    public float calcFriendlyManaEfficiency(long friendlyManaUsed, long friendlyTotalMana, long friendlyManaSaved) {
        return (float) friendlyManaUsed / (friendlyTotalMana - friendlyManaSaved);
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

    private long getMana(Set<Turn> turns, Player player, ToIntFunction<Turn> turnToIntFunction) {
        IntSummaryStatistics stats = turns.stream().filter(t -> t.getWhoseTurn() == player).collect(Collectors.summarizingInt(turnToIntFunction));
        return stats.getSum();
    }

    public float calcOpposingManaEfficiency(long opposingManaUsed, long opposingTotalMana, long opposingManaSaved) {
        return (float) opposingManaUsed / (opposingTotalMana - opposingManaSaved);
    }
}