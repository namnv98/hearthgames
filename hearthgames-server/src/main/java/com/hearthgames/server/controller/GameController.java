package com.hearthgames.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.service.GameAnalysisService;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.service.GameParserService;
import com.hearthgames.server.service.GamePlayingService;
import com.hearthgames.server.service.RawLogProcessingService;
import com.hearthgames.server.util.GameCompressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;

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
    public ModelAndView getGame(@PathVariable Long gameId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("game");

        GamePlayed gamePlayed = gameService.getById(gameId);
        if (gamePlayed != null) {
            byte[] bytes = gamePlayed.getRawGame();
            String logfile = GameCompressionUtils.decompressGameData(bytes);
            String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
            String[] lines = logfile.split(splitStr);

            List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
            if (rawGameDatas != null && rawGameDatas.size() == 1) {
                RawGameData rawGameData = rawGameDatas.get(0);
                GameContext context = gameParserService.parseLines(rawGameData.getLines());
                GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                GenericTable cardInfo = gameAnalysisService.getCardSummary(result, context);
                VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
                List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, context);
                List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, context);
                List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, context);
                List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context);
                GenericTable tradeInfo = gameAnalysisService.getTradeInfo(result, context);
                GenericTable manaInfo = gameAnalysisService.getManaInfo(result, context);

                modelAndView.addObject("cardInfos", Collections.singletonList(cardInfo));
                modelAndView.addObject("versusInfo", versusInfo);
                modelAndView.addObject("healthArmorInfos", healthArmorInfos);
                modelAndView.addObject("manaInfos", Collections.singletonList(manaInfo));
                modelAndView.addObject("tradeInfos", Collections.singletonList(tradeInfo));
                modelAndView.addObject("boardControlInfos", boardControlInfos);
                modelAndView.addObject("cardAdvantageInfos", cardAdvantageInfos);

                ObjectMapper mapper = new ObjectMapper();
                try {
                    String jsonInString = mapper.writeValueAsString(turnInfos);
                    modelAndView.addObject("turnInfos", jsonInString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                gamePlayed.setJustAdded(false);
                gameService.saveGamePlayed(gamePlayed);

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
}
