package com.hearthlogs.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.CardSets;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.raw.filter.AssetFilter;
import com.hearthlogs.server.match.raw.filter.BobFilter;
import com.hearthlogs.server.match.raw.filter.PowerFilter;
import com.hearthlogs.server.match.stats.domain.MatchStatistics;
import com.hearthlogs.server.match.raw.domain.RawMatchData;
import com.hearthlogs.server.match.view.domain.HealthArmorSummary;
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
    MatchResultRenderingService matchResultRenderingService;

    @Before
    public void init() throws IOException {
        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class));
        matchParserService = new MatchParserService();
        matchPlayingService = new MatchPlayingService(cardService);
        matchStatisticalAnalysisService = new MatchStatisticalAnalysisService();
        rawLogProcessingService = new RawLogProcessingService(new PowerFilter(), new BobFilter(), new AssetFilter());
        matchResultRenderingService = new MatchResultRenderingService();
    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game7-Shaman-vs-Hunter"));

        List<RawMatchData> rawMatchData = rawLogProcessingService.processLogFile(lines);

        ParseContext context = matchParserService.parseLines(rawMatchData.get(0).getLines());
        MatchResult result = matchPlayingService.processMatch(context, rawMatchData.get(0).getRank());
        MatchStatistics stats = matchStatisticalAnalysisService.calculateStatistics(result, context);

        HealthArmorSummary healthArmorSummary = matchResultRenderingService.getHealthSummary(result, context);


        result.setFriendly(context.getFriendlyPlayer());
        result.setOpposing(context.getOpposingPlayer());

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