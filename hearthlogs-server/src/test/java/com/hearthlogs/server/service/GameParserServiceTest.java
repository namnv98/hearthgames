package com.hearthlogs.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.game.analysis.*;
import com.hearthlogs.server.game.analysis.domain.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.CardSets;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.log.filter.AssetLineFilter;
import com.hearthlogs.server.game.log.filter.BobLineFilter;
import com.hearthlogs.server.game.log.filter.PowerLineFilter;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.hearthpwn.CardLinks;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.URL;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GameParserServiceTest {

    GamePlayingService gamePlayingService;
    GameParserService gameParserService;
    RawLogProcessingService rawLogProcessingService;
    GameAnalysisService gameAnalysisService;

    @Before
    public void init() throws IOException {
        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class),
                new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("HearthPwn.json"), CardLinks.class));
        gameParserService = new GameParserService();
        gamePlayingService = new GamePlayingService(cardService);
        rawLogProcessingService = new RawLogProcessingService(new PowerLineFilter(), new BobLineFilter(), new AssetLineFilter());
        gameAnalysisService = new GameAnalysisService(new ManaInfoAnalyzer(), new HealthArmorInfoAnalyzer(), new VersusInfoAnalyzer(), new CardInfoAnalyzer(), new BoardControlInfoAnalyzer(), new CardAdvantageInfoAnalyzer(), new TurnInfoAnalyzer());
    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game10-warlock-vs-warrior"));

        List<RawMatchData> rawMatchData = rawLogProcessingService.processLogFile(lines);

        GameContext context = gameParserService.parseLines(rawMatchData.get(0).getLines());
        GameResult result = gamePlayingService.processMatch(context, rawMatchData.get(0).getRank());

        CardInfo cardInfo = gameAnalysisService.getCardInfo(result, context);
        VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
        List<HealthArmorInfo> healthArmorInfos = gameAnalysisService.getHealthArmorInfo(result, context);
        ManaInfo manaInfo = gameAnalysisService.getManaInfo(result, context);
        List<BoardControlInfo> boardControlInfos = gameAnalysisService.getBoardControlInfo(result, context);
        List<CardAdvantageInfo> cardAdvantageInfos = gameAnalysisService.getCardAdvantageInfo(result, context);
        List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context);

        System.out.println();
    }

    private byte[] getData(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        assert url != null;
        File file = new File(url.getFile());
        return FileUtils.readFileToByteArray(file);
    }
}