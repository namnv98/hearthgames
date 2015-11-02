package com.hearthlogs.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.parse.domain.CardSets;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.handler.*;
import com.hearthlogs.server.match.play.domain.ActionFactory;
import com.hearthlogs.server.match.raw.filter.AssetFilter;
import com.hearthlogs.server.match.raw.filter.BobFilter;
import com.hearthlogs.server.match.raw.filter.PowerFilter;
import com.hearthlogs.server.match.stats.domain.MatchStatistics;
import com.hearthlogs.server.match.raw.domain.RawMatchData;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.URL;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MatchParserServiceTest {

    MatchPlayingService matchPlayingService;
    MatchParserService matchParserService;
    MatchStatisticalAnalysisService matchStatisticalAnalysisService;
    RawLogProcessingService rawLogProcessingService;

    @Before
    public void init() throws IOException {
        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class));
        ActionFactory actionFactory = new ActionFactory();
        ActionHandler actionHandler = new ActionHandler(actionFactory);
        CardHandler cardHandler = new CardHandler(actionFactory);
        GameHandler gameHandler = new GameHandler(actionFactory);
        PlayerHandler playerHandler = new PlayerHandler(actionFactory);
        matchParserService = new MatchParserService();
        matchPlayingService = new MatchPlayingService(cardService, actionHandler, cardHandler, gameHandler, playerHandler);
        matchStatisticalAnalysisService = new MatchStatisticalAnalysisService();
        rawLogProcessingService = new RawLogProcessingService(new PowerFilter(), new BobFilter(), new AssetFilter());
    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game3"));

        List<RawMatchData> rawMatchData = rawLogProcessingService.processLogFile(lines);

        ParsedMatch parsedMatch = matchParserService.parseLines(rawMatchData.get(1).getLines());
        MatchResult result = matchPlayingService.processMatch(parsedMatch, rawMatchData.get(1).getRank());
        MatchStatistics stats = matchStatisticalAnalysisService.calculateStatistics(result, parsedMatch);

        result.setFriendly(parsedMatch.getFriendlyPlayer());
        result.setOpposing(parsedMatch.getOpposingPlayer());

        System.out.println(stats.getFriendlyManaEfficiency());
        System.out.println(stats.getOpposingManaEfficiency());
        System.out.println(stats.getFriendlyManaUsed() + " / " + stats.getFriendlyTotalMana());
        System.out.println(stats.getOpposingManaUsed() + " / " + stats.getOpposingTotalMana());
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