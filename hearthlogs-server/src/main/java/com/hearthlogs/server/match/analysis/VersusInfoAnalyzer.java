package com.hearthlogs.server.match.analysis;

import com.hearthlogs.server.match.analysis.domain.VersusInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import org.springframework.stereotype.Component;

@Component
public class VersusInfoAnalyzer implements Analyzer<VersusInfo> {
    @Override
    public VersusInfo analyze(MatchResult result, ParseContext context) {
        VersusInfo info = new VersusInfo();

        info.setWinner(result.getWinner().getName());
        info.setWinnerClass(result.getWinnerClass());
        info.setLoser(result.getLoser().getName());
        info.setLoserClass(result.getLoserClass());
        if (result.getQuitter() != null) {
            info.setQuitter(result.getQuitter() == result.getWinner() ? result.getWinner().getName() : result.getLoser().getName());
            info.setQuitterClass(result.getQuitter() == result.getWinner() ? result.getWinnerClass() : result.getLoserClass());
        }

        String friendlyClass = result.getWinner() == result.getFriendly() ? result.getWinnerClass() : result.getLoserClass();
        String opposingClass = result.getWinner() == result.getOpposing() ? result.getWinnerClass() : result.getLoserClass();
        info.setFriendlyClass(friendlyClass);
        info.setOpposingClass(opposingClass);
        info.setFriendlyName(context.getFriendlyPlayer().getName());
        info.setOpposingName(context.getOpposingPlayer().getName());

        return info;
    }
}
