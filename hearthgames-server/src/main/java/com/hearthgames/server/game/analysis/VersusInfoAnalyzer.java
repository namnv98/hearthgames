package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.util.DurationUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

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

        String friendlyClass = result.getWinner() == context.getFriendlyPlayer() ? result.getWinnerClass() : result.getLoserClass();
        String opposingClass = result.getWinner() == context.getOpposingPlayer() ? result.getWinnerClass() : result.getLoserClass();
        info.setFriendlyClass(friendlyClass);
        info.setOpposingClass(opposingClass);
        info.setFriendlyName(context.getFriendlyPlayer().getName());
        info.setOpposingName(context.getOpposingPlayer().getName());

        Card friendlyHeroCard = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
        Card opposingHeroCard = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());

        info.setFriendlyHeroCardId(friendlyHeroCard.getCardid());
        info.setOpposingHeroCardId(opposingHeroCard.getCardid());

        Turn firstTurn = result.getTurns().iterator().next();
        Turn lastTurn = result.getCurrentTurn();

        if (firstTurn.getStartDateTime() != null) {
            Duration duration = Duration.between(firstTurn.getStartDateTime(), lastTurn.getEndDateTime());
            info.setDuration(DurationUtils.formatDuration(duration));
        } else {
            info.setDuration("Not Recorded");
        }

        return info;
    }
}
