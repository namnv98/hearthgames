package com.hearthgames.server.service;

import com.hearthgames.server.HearthGamesServerApplication;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.domain.RawGameError;
import com.hearthgames.server.database.repository.GamePlayedRepository;
import com.hearthgames.server.database.repository.RawMatchErrorRepository;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.hsreplay.HSReplayHandler;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.util.GameCompressionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
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
    public void shouldPlayHSReplay() throws Exception {

        String xml = FileUtils.readFileToString(new File("/Users/milice/github/hearthgames/hearthgames-server/src/main/resources/test-data/ranked.hsreplay.xml"));
        String[] rawGamesXml = xml.split("<Game ");
        List<String> rawGames = new ArrayList<>();
        for (String rawGame: rawGamesXml) {
            if (rawGame.startsWith("ts"))
                rawGames.add("<?xml version=\"1.0\" ?><Game "+rawGame);
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        HSReplayHandler handler;
        for (String rawGame: rawGames) {
            handler = new HSReplayHandler();
            saxParser.parse(new InputSource(new StringReader(rawGame)), handler);

            GameContext context = handler.getContext();
            String accountId = context.getFriendlyPlayer().getGameAccountIdLo();

            GameResult result = gamePlayingService.processGame(context, null);

            RawGameData rawGameData = new RawGameData();
            rawGameData.setXml(rawGame);

//            GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);

//            gameService.saveGamePlayed(gamePlayed, context, result, false);
            System.out.println();


        }

    }

    public static String getXPathValue(String stringCompile, String stringSource) {
        StringBuilder stringBuilder = new StringBuilder();
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath path = pathFactory.newXPath();

        try {
            XPathExpression pathExpression = path.compile(stringCompile);
            Object result = pathExpression.evaluate(new InputSource(new StringReader(stringSource)), XPathConstants.STRING);
            if (result instanceof String) {
                stringBuilder.append(result.toString());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Test
    public void shouldPlayUncompressedGame() throws IOException {

        List<String> lines = FileUtils.readLines(new File("c:\\games\\output_log.txt"));

        String accountId = "";
        List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(lines);
        for (RawGameData rawGameData : rawGameDatas) {

            try {
                GameContext context = gameParserService.parseLines(rawGameData.getLines());
                accountId = context.getFriendlyPlayer().getGameAccountIdLo();

                GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                GenericTable cardInfo = gameAnalysisService.getCardSummary(result, context, rawGameData);
                VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context, rawGameData);
                List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, context, rawGameData);
                GenericTable manaInfo = gameAnalysisService.getManaInfo(result, context, rawGameData);
                GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, context, rawGameData);
                List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, context, rawGameData);
                List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, context, rawGameData);
                List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context, rawGameData);

                GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }


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

            List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
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