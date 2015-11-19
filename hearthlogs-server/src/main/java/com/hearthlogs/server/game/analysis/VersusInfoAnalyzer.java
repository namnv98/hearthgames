package com.hearthlogs.server.game.analysis;

import com.hearthlogs.server.game.analysis.domain.VersusInfo;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import org.springframework.stereotype.Component;

@Component
public class VersusInfoAnalyzer implements Analyzer<VersusInfo> {
    @Override
    public VersusInfo analyze(GameResult result, GameContext context) {
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

        Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
        Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());

        info.setFriendlyHeroCardId(friendlyHeroCard.getCardid());
        info.setOpposingHeroCardId(opposingHeroCard.getCardid());

        return info;
    }
}