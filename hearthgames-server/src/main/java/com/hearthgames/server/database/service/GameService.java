package com.hearthgames.server.database.service;

import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.*;
import com.hearthgames.server.database.repository.*;
import com.hearthgames.server.game.log.domain.GameType;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.solr.SolrService;
import com.hearthgames.server.util.GameCompressionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private GamePlayedRepository gamePlayedRepository;

    @Autowired
    private RawMatchErrorRepository rawMatchErrorRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ArenaRunRepository arenaRunRepository;

    @Autowired
    private PlayerWinLossSummaryRepository playerWinLossSummaryRepository;

    @Autowired
    private SolrService solrService;

    public GamePlayed createGamePlayed(RawGameData rawGameData, GameState gameState, GameResult result, UserInfo userInfo) {

        GamePlayed gamePlayed = new GamePlayed();
        gamePlayed.setJustAdded(true);
        if (userInfo != null) {
            gamePlayed.setBattletag(userInfo.getBattletag());
            gamePlayed.setBattletagId(userInfo.getId());
        }
        LocalDateTime now = LocalDateTime.now();
        if (rawGameData.getXml() == null) {
            LocalDateTime startTime = rawGameData.getLines().get(0).getDateTime();
            gamePlayed.setStartTime(startTime == null ? now : startTime);
            LocalDateTime endTime = rawGameData.getLines().get(rawGameData.getLines().size()-1).getDateTime();
            gamePlayed.setEndTime(endTime == null ? now : endTime);
            gamePlayed.setRawGame(GameCompressionUtils.compress(linesToString(rawGameData.getRawLines())));
            gamePlayed.setRawGameType(1);
        } else {
            gamePlayed.setRawGame(GameCompressionUtils.compress(rawGameData.getXml()));
            gamePlayed.setStartTime(now);
            gamePlayed.setEndTime(now);
            gamePlayed.setRawGameType(2);
        }
        gamePlayed.setFriendlyGameAccountId(gameState.getFriendlyPlayer().getGameAccountIdLo());
        gamePlayed.setOpposingGameAccountId(gameState.getOpposingPlayer().getGameAccountIdLo());
        gamePlayed.setRank(rawGameData.getGameType() == GameType.RANKED ? rawGameData.getRank() : null);
        gamePlayed.setFriendlyName(gameState.getFriendlyPlayer().getName());
        gamePlayed.setFriendlyClass(gameState.getFriendlyPlayer().getPlayerClass() == null ? "unknown" : gameState.getFriendlyPlayer().getPlayerClass());
        gamePlayed.setOpposingName(gameState.getOpposingPlayer().getName());
        gamePlayed.setOpposingClass(gameState.getOpposingPlayer().getPlayerClass() == null ? "unknown" : gameState.getOpposingPlayer().getPlayerClass());
        gamePlayed.setWinner(result.getWinner());
        gamePlayed.setWinnerClass(result.getWinnerClass() == null ? "unknown" : result.getWinnerClass());
        gamePlayed.setTurns(result.getTurns().size());
        gamePlayed.setGameType(rawGameData.getGameType().getType());

        String friendlyStartingCards = StringUtils.join(result.getFriendlyStartingCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
        String friendlyMulliganCards = StringUtils.join(result.getFriendlyMulliganedCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
        gamePlayed.setFriendlyStartingCards(friendlyStartingCards);
        gamePlayed.setFriendlyMulliganCards(friendlyMulliganCards);

        if (rawGameData.getArenaDeckId() != null) {
            gamePlayed.setArenaDeckId(rawGameData.getArenaDeckId());
            gamePlayed.setFriendlyDeckCards(StringUtils.join(rawGameData.getArenaDeckCards(), ","));
        } else {
            String friendlyDeckCards = StringUtils.join(result.getFriendlyDeckCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
            gamePlayed.setFriendlyDeckCards(friendlyDeckCards);
        }

        String opposingStartingCards = StringUtils.join(result.getOpposingStartingCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
        String opposingMulliganCards = StringUtils.join(result.getOpposingMulliganedCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
        String opposingDeckCards = StringUtils.join(result.getOpposingDeckCards().stream().filter(card -> !StringUtils.isEmpty(card.getCardid())).map(Card::getCardid).collect(Collectors.toList()),",");
        gamePlayed.setOpposingStartingCards(opposingStartingCards);
        gamePlayed.setOpposingMulliganCards(opposingMulliganCards);
        gamePlayed.setOpposingDeckCards(opposingDeckCards);

        return gamePlayed;
    }

    public void saveGamePlayed(GamePlayed gamePlayed, GameState gameState, GameResult result, boolean index) {
        gamePlayedRepository.save(gamePlayed);
        if (index) {
            try {
                solrService.indexGame(gamePlayed, gameState, result);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public Iterable<PlayerWinLossSummary> getPlayers() {
        return playerWinLossSummaryRepository.findAll();
    }

    public Long getGamesPlayedCount(String id) {
        return gamePlayedRepository.countByFriendlyGameAccountId(id);
    }

    public List<GamePlayed> getGamesPlayed(String id) {
        return gamePlayedRepository.findByFriendlyGameAccountId(id);
    }

    public List<ArenaRun> getArenaRuns(String id) {
        return arenaRunRepository.findByGameAccountId(id);
    }

    public List<GamePlayed> getGamesPlayed(PageRequest pageRequest, int gameType) {
        return gamePlayedRepository.findAllByGameType(pageRequest, gameType);
    }

    public List<GamePlayed> getArenaGamesByDeckId(String deckId) {
        return gamePlayedRepository.findAllByArenaDeckId(deckId);
    }

    public List<ArenaRun> getArenaRuns(PageRequest pageRequest) {
        return arenaRunRepository.findAll(pageRequest).getContent();
    }

    public Long getArenaRunCount() {
        return arenaRunRepository.count();
    }

    public Long getGamesPlayedCount(int gameType) {
        return gamePlayedRepository.countByGameType(gameType);
    }

    public GamePlayed getById(Long id) {
        return gamePlayedRepository.findOne(id);
    }

    public GamePlayed findSameGame(GamePlayed gamePlayed) {
        List<GamePlayed> gamesPlayed = gamePlayedRepository.findByFriendlyGameAccountIdAndOpposingGameAccountId(gamePlayed.getFriendlyGameAccountId(), gamePlayed.getOpposingGameAccountId());
        for (GamePlayed game: gamesPlayed) {
            if (game.isSameGame(gamePlayed)) {
                return game;
            }
        }
        return null;
    }

    public void saveRawGameError(RawGameData rawGameData, LocalDateTime startTime, LocalDateTime endTime) {
        RawGameError rawGameError = new RawGameError();
        rawGameError.setRawGame(GameCompressionUtils.compress(linesToString(rawGameData.getRawLines())));
        rawGameError.setRank(rawGameData.getRank());
        rawGameError.setStartTime(startTime);
        rawGameError.setEndTime(endTime);
        rawMatchErrorRepository.save(rawGameError);
    }

    public String getGameAccountId(String battletag) {
        Account account = accountRepository.findFirstByBattletag(battletag);
        return account != null ? account.getGameAccountId() : null;
    }

    public Account createAccount(UserInfo userInfo) {
        Account account = new Account();
        account.setBattletag(userInfo.getBattletag());
        account.setBattletagId(userInfo.getId());
        return accountRepository.save(account);
    }

    public void updateGameAccountId(String battletag, String gameAccountId) {
        Account account = accountRepository.findFirstByBattletag(battletag);
        if (account != null) {
            account.setGameAccountId(gameAccountId);
            accountRepository.save(account);
        }
    }

    public boolean doesAccountExist(String battletag) {
        Account account = accountRepository.findFirstByBattletag(battletag);
        return account != null;
    }

    public Account getAccount(String battletag) {
        return accountRepository.findFirstByBattletag(battletag);
    }

    private String linesToString(List<String> lines) {
        return StringUtils.join(lines, "\n");
    }

}
