package com.hearthgames.server.service;

import com.hearthgames.server.HearthGamesServerApplication;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.repository.GamePlayedRepository;
import com.hearthgames.server.database.repository.RawMatchErrorRepository;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.hsreplay.HSReplayHandler;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.play.GameResult;
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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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

        File file = new File("/Users/milice/github/hearthgames/hearthgames-server/src/main/resources/test-data/ranked.hsreplay.xml");
        org.junit.Assume.assumeTrue(file.exists());

        String xml = FileUtils.readFileToString(file);
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

            GameState gameState = handler.getGameState();
            String accountId = gameState.getFriendlyPlayer().getGameAccountIdLo();

            GameResult result = gamePlayingService.processGame(gameState, null);

            RawGameData rawGameData = new RawGameData();
            rawGameData.setXml(rawGame);

//            GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, gameState, result, null);

//            gameService.saveGamePlayed(gamePlayed, gameState, result, false);
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

        File file = new File("c:\\games\\output_log.txt");
        org.junit.Assume.assumeTrue(file.exists());

        List<String> lines = FileUtils.readLines(file);

        String accountId = "";
        List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(lines);
        for (RawGameData rawGameData : rawGameDatas) {

            try {
                GameState gameState = gameParserService.parseLines(rawGameData.getLines());
                accountId = gameState.getFriendlyPlayer().getGameAccountIdLo();

                GameResult result = gamePlayingService.processGame(gameState, rawGameData.getRank());

                GenericTable cardInfo = gameAnalysisService.getCardSummary(result, gameState, rawGameData);
                VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, gameState, rawGameData);
                List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, gameState, rawGameData);
                GenericTable manaInfo = gameAnalysisService.getManaInfo(result, gameState, rawGameData);
                GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, gameState, rawGameData);
                List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, gameState, rawGameData);
                List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, gameState, rawGameData);
                List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, gameState, rawGameData);

                GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, gameState, result, null);
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }


        }
        List<GamePlayed> gamesPlayed = gameService.getGamesPlayed(accountId);
        System.out.println();
    }

//    @Test
//    public void shouldFixRawGameError() throws IOException {
//
//        Iterable<RawGameError> errors = rawMatchErrorRepository.findAll();
//
//        for (RawGameError rawGameError: errors) {
//
//            String gameData = GameCompressionUtils.decompressGameData(rawGameError.getRawGame());
//            String[] lines = gameData.split("\n");
//
//            FileUtils.writeStringToFile(new File("c:\\games\\error"+rawGameError.getId()), gameData);
//
//            List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
//            try {
//                for (RawGameData rawGameData: rawGameDatas) {
//                    GameContext gameState = gameParserService.parseLines(rawGameData.getLines());
//                    GameResult result = gamePlayingService.processGame(gameState, rawGameData.getRank());
//
//                    GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, gameState, result, null);
//                    GamePlayed sameGame = gameService.findSameGame(gamePlayed);
//                    if (sameGame == null) {
//                        gameService.saveGamePlayed(gamePlayed, gameState, result, true);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            System.out.println();
//
//        }
//
//
//    }

}