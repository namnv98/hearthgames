package com.hearthlogs.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.match.domain.CardSets;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import com.hearthlogs.server.match.handler.*;
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

    MatchService matchService;
    MatchParserService matchParserService;

    @Before
    public void init() throws IOException {
        CardService cardService = new CardService(new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class));
        CardHandler cardHandler = new CardHandler();
        GameHandler gameHandler = new GameHandler();
        PlayerHandler playerHandler = new PlayerHandler();
        matchParserService = new MatchParserService();
        matchService = new MatchService(cardService, cardHandler, gameHandler, playerHandler);
    }

//    @Test
//    public void shouldPlaySampleGame1() throws IOException {
//
//        String match = matchService.decompressGameData(getData("fullgame.test"));
////        FileUtils.writeStringToFile(new File("uncompressed-fullgame.test"), match, "UTF-8");
//
//        String[] lines = match.split("\n");
//
//        MatchActivityParser parser = new MatchActivityParser();
//        MatchContext context = new MatchContext();
//        for (String line: lines) {
//            parser.parse(context, line);
//        }
//        for (Activity activity: context.getActivities()) {
//            matchService.handle(context, activity);
//        }
//
//    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\game2"));

        MatchContext context = matchParserService.processMatch(lines);
        MatchResult result = matchService.processMatch(context, "1");

        result.setFriendly(context.getFriendlyPlayer());
        result.setOpposing(context.getOpposingPlayer());

        System.out.println(result.getFriendlyManaEfficiency());
        System.out.println(result.getOpposingManaEfficiency());
        System.out.println(result.getFriendlyManaUsed() + " / " + result.getFriendlyTotalMana());
        System.out.println(result.getOpposingManaUsed() + " / " + result.getOpposingTotalMana());
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