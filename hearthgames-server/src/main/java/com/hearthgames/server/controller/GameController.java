package com.hearthgames.server.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.hsreplay.HSReplayHandler;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.service.GameAnalysisService;
import com.hearthgames.server.service.GameParserService;
import com.hearthgames.server.service.GamePlayingService;
import com.hearthgames.server.service.RawLogProcessingService;
import com.hearthgames.server.util.GameCompressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameAnalysisService gameAnalysisService;

    @RequestMapping(value = {"/game/{gameId}", "/mygame/{gameId}"})
    public ModelAndView getGame(@PathVariable Long gameId) throws ParserConfigurationException, SAXException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("game");

        GamePlayed gamePlayed = gameService.getById(gameId);
        if (gamePlayed != null) {
            RawGameData rawGameData;
            GameState gameState;

            if (gamePlayed.getRawGameType() == 1) {
                rawGameData = getRawGameDataFromLogFile(gamePlayed.getRawGame());
                gameState = getGameContextFromLogFile(rawGameData);
            } else {
                rawGameData = getRawGameDataFromXml(gamePlayed.getRawGame());
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                HSReplayHandler handler = new HSReplayHandler();
                saxParser.parse(new InputSource(new StringReader(rawGameData.getXml())), handler);
                gameState = handler.getGameState();
            }
            if (rawGameData != null && gameState != null) {
                GameResult result = gamePlayingService.processGame(gameState, rawGameData.getRank());

                GenericTable cardInfo = gameAnalysisService.getCardSummary(result, gameState, rawGameData);
                VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, gameState, rawGameData);
                List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, gameState, rawGameData);
                List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, gameState, rawGameData);
                List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, gameState, rawGameData);
                List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, gameState, rawGameData);
                GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, gameState, rawGameData);
                GenericTable manaInfo = gameAnalysisService.getManaInfo(result, gameState, rawGameData);

                modelAndView.addObject("cardInfos", Collections.singletonList(cardInfo));
                modelAndView.addObject("versusInfo", versusInfo);
                modelAndView.addObject("healthArmorInfos", healthArmorInfos);
                modelAndView.addObject("manaInfos", Collections.singletonList(manaInfo));
                modelAndView.addObject("tradeInfos", Collections.singletonList(tradeInfo));
                modelAndView.addObject("boardControlInfos", boardControlInfos);
                modelAndView.addObject("cardAdvantageInfos", cardAdvantageInfos);

                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                try {
                    String jsonInString = mapper.writeValueAsString(turnInfos);
                    modelAndView.addObject("turnInfos", jsonInString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                modelAndView.addObject("gameId", gameId);

                if (gamePlayed.getGameType() == null) {
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    UserInfo userInfo = null;
                    if (principal != null && principal instanceof UserInfo) {
                        userInfo = (UserInfo) principal;
                    }
                    GamePlayed updatedGamePlayed = gameService.createGamePlayed(rawGameData, gameState, result, userInfo);
                    gameService.saveGamePlayed(updatedGamePlayed, gameState, result, false);
                } else {
                    gamePlayed.setJustAdded(false);
                    gameService.saveGamePlayed(gamePlayed, gameState, result, false);
                }
                //hack for Thymeleaf plugin - duplicate model properties
                if (false) {
                    WebContext webContext = new org.thymeleaf.context.WebContext(null, null, null);
                    webContext.setVariable("cardInfos", Collections.singletonList(cardInfo));
                    webContext.setVariable("versusInfo", versusInfo);
                    webContext.setVariable("healthArmorInfos", healthArmorInfos);
                    webContext.setVariable("manaInfos", Collections.singletonList(manaInfo));
                    webContext.setVariable("tradeInfos", Collections.singletonList(tradeInfo));
                    webContext.setVariable("boardControlInfos", boardControlInfos);
                    webContext.setVariable("cardAdvantageInfos", cardAdvantageInfos);
                    webContext.setVariable("turnInfos", turnInfos);
                }
            }
        }
        return modelAndView;
    }

    private RawGameData getRawGameDataFromXml(byte[] bytes) {
        RawGameData rawGameData = new RawGameData();
        rawGameData.setXml(GameCompressionUtils.decompressGameData(bytes));
        return rawGameData;
    }

    private RawGameData getRawGameDataFromLogFile(byte[] bytes ) {
        String logfile = GameCompressionUtils.decompressGameData(bytes);
        String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
        String[] lines = logfile.split(splitStr);

        List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
        if (rawGameDatas != null && rawGameDatas.size() == 1) {
            return rawGameDatas.get(0);
        }
        return null;
    }

    private GameState getGameContextFromLogFile(RawGameData rawGameData){
        return gameParserService.parseLines(rawGameData.getLines());
    }
}
