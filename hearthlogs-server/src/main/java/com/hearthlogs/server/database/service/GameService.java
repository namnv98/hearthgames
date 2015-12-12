package com.hearthlogs.server.database.service;

import com.hearthlogs.server.config.security.UserInfo;
import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.domain.RawMatchError;
import com.hearthlogs.server.database.repository.GamePlayedRepository;
import com.hearthlogs.server.database.repository.RawMatchErrorRepository;
import com.hearthlogs.server.game.log.domain.RawGameData;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.util.GameCompressionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GamePlayedRepository gamePlayedRepository;

    @Autowired
    private RawMatchErrorRepository rawMatchErrorRepository;

    public GamePlayed createGamePlayed(RawGameData rawGameData, GameContext context, GameResult result, UserInfo userInfo) {

        GamePlayed gamePlayed = new GamePlayed();
        gamePlayed.setJustAdded(true);
        if (userInfo != null) {
            gamePlayed.setBattletag(userInfo.getBattletag());
            gamePlayed.setBattletagId(userInfo.getId());
        }
        gamePlayed.setRawGame(GameCompressionUtils.compress(linesToString(rawGameData.getRawLines())));
        LocalDateTime startTime = rawGameData.getLines().get(0).getDateTime();
        LocalDateTime now = LocalDateTime.now();
        gamePlayed.setStartTime(startTime == null ? now : startTime);
        LocalDateTime endTime = rawGameData.getLines().get(rawGameData.getLines().size()-1).getDateTime();
        gamePlayed.setEndTime(endTime == null ? now : endTime);
        gamePlayed.setFriendlyGameAccountId(context.getFriendlyPlayer().getGameAccountIdLo());
        gamePlayed.setOpposingGameAccountId(context.getOpposingPlayer().getGameAccountIdLo());
        gamePlayed.setRank(rawGameData.getRank());
        gamePlayed.setFriendlyName(context.getFriendlyPlayer().getName());
        gamePlayed.setFriendlyClass(context.getFriendlyPlayer().getPlayerClass());
        gamePlayed.setOpposingName(context.getOpposingPlayer().getName());
        gamePlayed.setOpposingClass(context.getOpposingPlayer().getPlayerClass());
        gamePlayed.setWinner(result.getWinner().getName());
        gamePlayed.setWinnerClass(result.getWinnerClass());
        gamePlayed.setTurns(result.getTurns().size());

        String friendlyStartingCards = StringUtils.join(result.getFriendlyStartingCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        String friendlyMulliganCards = StringUtils.join(result.getFriendlyMulliganedCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        String friendlyDeckCards = StringUtils.join(result.getFriendlyDeckCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        gamePlayed.setFriendlyStartingCards(friendlyStartingCards);
        gamePlayed.setFriendlyMulliganCards(friendlyMulliganCards);
        gamePlayed.setFriendlyDeckCards(friendlyDeckCards);

        String opposingStartingCards = StringUtils.join(result.getOpposingStartingCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        String opposingMulliganCards = StringUtils.join(result.getOpposingMulliganedCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        String opposingDeckCards = StringUtils.join(result.getOpposingDeckCards().stream().filter(card -> card.getCardid() != null).map(Card::getCardid).collect(Collectors.toList()),",");
        gamePlayed.setOpposingStartingCards(opposingStartingCards);
        gamePlayed.setOpposingMulliganCards(opposingMulliganCards);
        gamePlayed.setOpposingDeckCards(opposingDeckCards);

        return gamePlayed;
    }

    public void saveGamePlayed(GamePlayed gamePlayed) {
        gamePlayedRepository.save(gamePlayed);
    }

    public List<GamePlayed> getGamesPlayed(String id) {
        return gamePlayedRepository.findByFriendlyGameAccountId(id);
    }

    public GamePlayed getById(Long id) {
        return gamePlayedRepository.findOne(id);
    }

    public boolean hasGameBeenPlayed(GamePlayed gamePlayed) {
        List<GamePlayed> gamesPlayed = gamePlayedRepository.findByFriendlyGameAccountIdAndOpposingGameAccountId(gamePlayed.getFriendlyGameAccountId(), gamePlayed.getOpposingGameAccountId());
        for (GamePlayed game: gamesPlayed) {
            if (game.isSameGame(gamePlayed)) {
                return true;
            }
        }
        return false;
    }

    public void saveRawMatchError(RawGameData rawGameData) {
        RawMatchError rawMatchError = new RawMatchError();
        rawMatchError.setRawGame(GameCompressionUtils.compress(linesToString(rawGameData.getRawLines())));
        rawMatchErrorRepository.save(rawMatchError);
    }

    public String getGameAccountId(String battletag) {
        GamePlayed gamePlayed = gamePlayedRepository.findFirstByBattletag(battletag);
        return gamePlayed != null ? gamePlayed.getFriendlyGameAccountId() : null;
    }

    private String linesToString(List<String> lines) {
        return StringUtils.join(lines, "\n");
    }

}
