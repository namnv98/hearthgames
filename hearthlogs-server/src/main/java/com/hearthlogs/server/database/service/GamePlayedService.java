package com.hearthlogs.server.database.service;

import com.hearthlogs.server.config.security.UserInfo;
import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.repository.GamePlayedRepository;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

@Service
public class GamePlayedService {

    @Autowired
    private GamePlayedRepository gamePlayedRepository;

    public GamePlayed createGamePlayed(RawMatchData rawMatchData, GameContext context, GameResult result, UserInfo userInfo) {

        GamePlayed gamePlayed = new GamePlayed();
        if (userInfo != null) {
            gamePlayed.setBattletag(userInfo.getBattletag());
            gamePlayed.setBattletagId(userInfo.getId());
        }
        gamePlayed.setRawGame(compress(linesToString(rawMatchData.getRawLines())));
        gamePlayed.setStartTime(rawMatchData.getLines().get(0).getDateTime());
        gamePlayed.setEndTime(rawMatchData.getLines().get(rawMatchData.getLines().size()-1).getDateTime());
        gamePlayed.setFriendlyGameAccountId(context.getFriendlyPlayer().getGameAccountIdLo());
        gamePlayed.setOpposingGameAccountId(context.getOpposingPlayer().getGameAccountIdLo());
        gamePlayed.setRank(rawMatchData.getRank());
        gamePlayed.setFriendlyName(context.getFriendlyPlayer().getName());
        gamePlayed.setFriendlyClass(context.getFriendlyPlayer().getPlayerClass());
        gamePlayed.setOpposingName(context.getOpposingPlayer().getName());
        gamePlayed.setOpposingClass(context.getOpposingPlayer().getPlayerClass());
        gamePlayed.setWinner(result.getWinner().getName());
        gamePlayed.setWinnerClass(result.getWinnerClass());

        gamePlayedRepository.save(gamePlayed);

        return gamePlayed;
    }

    public List<GamePlayed> getGamesPlayed(String id) {
        return gamePlayedRepository.findByFriendlyGameAccountId(id);
    }

    public GamePlayed getById(Long id) {
        return gamePlayedRepository.findOne(id);
    }

    public boolean hasGameBeenPlayed(RawMatchData rawMatchData, GameContext context) {
        return gamePlayedRepository.findByFriendlyGameAccountIdAndOpposingGameAccountIdAndStartTime(
            context.getFriendlyPlayer().getGameAccountIdLo(), context.getOpposingPlayer().getGameAccountIdLo(), rawMatchData.getLines().get(0).getDateTime()
        ) != null;
    }

    public String getGameAccountId(String battletag) {
        GamePlayed gamePlayed = gamePlayedRepository.findFirstByBattletag(battletag);
        return gamePlayed != null ? gamePlayed.getFriendlyGameAccountId() : null;
    }

    private String linesToString(List<String> lines) {
        return StringUtils.join(lines, "\n");
    }

    private byte[] compress(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream out = new DeflaterOutputStream(baos)){
            out.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }
}
