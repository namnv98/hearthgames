package com.hearthlogs.server.service;

import com.hearthlogs.server.HearthlogsServerApplication;
import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.repository.GamePlayedRepository;
import com.hearthlogs.server.database.service.GamePlayedService;
import com.hearthlogs.server.game.analysis.domain.*;
import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HearthlogsServerApplication.class)
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
    GamePlayedService gamePlayedService;

    @Autowired
    private GamePlayedRepository gamePlayedRepository;


    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game9-4huntergames"));

        String accountId = "";
        List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(lines);
        for (RawMatchData rawMatchData: rawMatchDatas) {

            GameContext context = gameParserService.parseLines(rawMatchData.getLines());
            accountId = context.getFriendlyPlayer().getGameAccountIdLo();

            GameResult result = gamePlayingService.processMatch(context, rawMatchData.getRank());

            GenericTable cardInfo = gameAnalysisService.getCardSummary(result, context);
            VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
            List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, context);
            GenericTable manaInfo = gameAnalysisService.getManaInfo(result, context);
            GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, context);
            List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, context);
            List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, context);
            List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context);

            GamePlayed gamePlayed = gamePlayedService.createGamePlayed(rawMatchData, context, result, null);
            System.out.println();

        }
        List<GamePlayed> gamesPlayed = gamePlayedService.getGamesPlayed(accountId);
        System.out.println();

        GamePlayed gamePlayed = gamePlayedRepository.findByFriendlyGameAccountIdAndOpposingGameAccountIdAndStartTime(
            gamesPlayed.get(0).getFriendlyGameAccountId(), gamesPlayed.get(0).getOpposingGameAccountId(), gamesPlayed.get(0).getStartTime()
        );
        System.out.println();
    }
}