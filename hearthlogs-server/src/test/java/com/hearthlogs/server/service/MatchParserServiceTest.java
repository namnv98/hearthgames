package com.hearthlogs.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.match.analysis.CardInfoAnalyzer;
import com.hearthlogs.server.match.analysis.HealthArmorInfoAnalyzer;
import com.hearthlogs.server.match.analysis.ManaInfoAnalyzer;
import com.hearthlogs.server.match.analysis.VersusInfoAnalyzer;
import com.hearthlogs.server.match.analysis.domain.CardInfo;
import com.hearthlogs.server.match.analysis.domain.VersusInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.CardSets;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.log.filter.AssetLineFilter;
import com.hearthlogs.server.match.log.filter.BobLineFilter;
import com.hearthlogs.server.match.log.filter.PowerLineFilter;
import com.hearthlogs.server.match.analysis.domain.ManaInfo;
import com.hearthlogs.server.match.log.domain.RawMatchData;
import com.hearthlogs.server.match.analysis.domain.HealthArmorInfo;
import com.hearthlogs.server.util.HearthPwnCardLinks;
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
    RawLogProcessingService rawLogProcessingService;
    MatchAnalysisService matchAnalysisService;

    @Before
    public void init() throws IOException {
        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class),
                new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("HearthPwn.json"), HearthPwnCardLinks.class));
        matchParserService = new MatchParserService();
        matchPlayingService = new MatchPlayingService(cardService);
        rawLogProcessingService = new RawLogProcessingService(new PowerLineFilter(), new BobLineFilter(), new AssetLineFilter());
        matchAnalysisService = new MatchAnalysisService(new ManaInfoAnalyzer(), new HealthArmorInfoAnalyzer(), new VersusInfoAnalyzer(), new CardInfoAnalyzer());
    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game7-Shaman-vs-Hunter"));

        List<RawMatchData> rawMatchData = rawLogProcessingService.processLogFile(lines);

        ParseContext context = matchParserService.parseLines(rawMatchData.get(0).getLines());
        MatchResult result = matchPlayingService.processMatch(context, rawMatchData.get(0).getRank());

        CardInfo cardInfo = matchAnalysisService.getCardInfo(result, context);
        VersusInfo versusInfo = matchAnalysisService.getVersusInfo(result, context);
        List<HealthArmorInfo> healthArmorInfos = matchAnalysisService.getHealthArmorInfo(result, context);
        ManaInfo manaInfo = matchAnalysisService.getManaInfo(result, context);

        System.out.println(manaInfo.getFriendlyManaEfficiency());
        System.out.println(manaInfo.getOpposingManaEfficiency());
        System.out.println(manaInfo.getFriendlyManaUsed() + " / " + manaInfo.getFriendlyTotalMana());
        System.out.println(manaInfo.getOpposingManaUsed() + " / " + manaInfo.getOpposingTotalMana());
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