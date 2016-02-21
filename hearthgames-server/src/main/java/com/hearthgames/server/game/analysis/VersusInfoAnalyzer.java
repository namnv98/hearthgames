package com.hearthgames.server.game.analysis;

import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.util.DurationUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class VersusInfoAnalyzer implements Analyzer<VersusInfo> {
    @Override
    public VersusInfo analyze(GameResult result, GameState gameState, RawGameData rawGameData) {
        VersusInfo info = new VersusInfo();

        info.setWinner(result.getWinner());
        info.setWinnerClass(result.getWinnerClass());
        info.setLoser(result.getLoser());
        info.setLoserClass(result.getLoserClass());
        if (result.getQuitter() != null) {
            info.setQuitter(result.getQuitter().getName());
            info.setQuitterClass(result.getQuitter().getPlayerClass());
        }

        String friendlyClass = result.getWinner().equals(gameState.getFriendlyPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();
        String opposingClass = result.getWinner().equals(gameState.getOpposingPlayer().getName()) ? result.getWinnerClass() : result.getLoserClass();
        info.setFriendlyClass(friendlyClass);
        info.setOpposingClass(opposingClass);
        info.setFriendlyName(gameState.getFriendlyPlayer().getName());
        info.setOpposingName(gameState.getOpposingPlayer().getName());

        Card friendlyHeroCard = gameState.getEntityById(gameState.getFriendlyPlayer().getHeroEntity());
        Card opposingHeroCard = gameState.getEntityById(gameState.getOpposingPlayer().getHeroEntity());

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
