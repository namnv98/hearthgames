package com.hearthgames.server.service;

import com.hearthgames.server.HearthGamesServerApplication;
import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.domain.RawGameError;
import com.hearthgames.server.database.repository.GamePlayedRepository;
import com.hearthgames.server.database.repository.RawMatchErrorRepository;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.util.GameCompressionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HearthGamesServerApplication.class)
public class GameParserServiceTest {

    @Autowired
    GamePlayingService gamePlayingService;

    @Autowired
    GameParserService gameParserService;

    @Autowired
    RawLogProcessingService rawLogProcessingService;

    @Autowired
    GameAnalysisService gameAnalysisService;

    @Autowired
    GameService gameService;

    @Autowired
    private GamePlayedRepository gamePlayedRepository;

    @Autowired
    private RawMatchErrorRepository rawMatchErrorRepository;


    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game9-4huntergames"));

        String accountId = "";
        List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(lines, false);
        for (RawGameData rawGameData : rawGameDatas) {

            GameContext context = gameParserService.parseLines(rawGameData.getLines());
            accountId = context.getFriendlyPlayer().getGameAccountIdLo();

            GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

            GenericTable cardInfo = gameAnalysisService.getCardSummary(result, context);
            VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
            List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, context);
            GenericTable manaInfo = gameAnalysisService.getManaInfo(result, context);
            GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, context);
            List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, context);
            List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, context);
            List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context);

            GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);
            System.out.println();

        }
        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(accountId);
        System.out.println();
    }

    @Test
    public void shouldFixRawGameError() throws IOException {

        Iterable<RawGameError> errors = rawMatchErrorRepository.findAll();

        for (RawGameError rawGameError: errors) {

            String gameData = GameCompressionUtils.decompressGameData(rawGameError.getRawGame());
            String[] lines = gameData.split("\n");

            FileUtils.writeStringToFile(new File("c:\\games\\error"+rawGameError.getId()), gameData);

            List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines), true);
            try {
                for (RawGameData rawGameData: rawGameDatas) {
                    GameContext context = gameParserService.parseLines(rawGameData.getLines());
                    GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                    GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);
                    GamePlayed sameGame = gameService.findSameGame(gamePlayed);
                    if (sameGame == null) {
                        gameService.saveGamePlayed(gamePlayed, context, result, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            System.out.println();

        }


    }

}