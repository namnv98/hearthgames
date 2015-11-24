package com.hearthlogs.server.controller;

import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.service.GamePlayedService;
import com.hearthlogs.server.game.analysis.domain.TurnInfo;
import com.hearthlogs.server.game.analysis.domain.VersusInfo;
import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.service.GameAnalysisService;
import com.hearthlogs.server.service.GameParserService;
import com.hearthlogs.server.service.GamePlayingService;
import com.hearthlogs.server.service.RawLogProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')")
public class GameController {

    @Autowired
    private GamePlayedService gamePlayedService;

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameAnalysisService gameAnalysisService;

    @RequestMapping(value = "/game/{gameId}")
    public ModelAndView getGame(@PathVariable Long gameId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("game");

        GamePlayed gamePlayed = gamePlayedService.getById(gameId);
        if (gamePlayed != null) {
            byte[] bytes = gamePlayed.getRawGame();
            String logfile = gameParserService.decompressGameData(bytes);
            String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
            String[] lines = logfile.split(splitStr);

            List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
            if (rawMatchDatas != null && rawMatchDatas.size() == 1) {
                RawMatchData rawMatchData = rawMatchDatas.get(0);
                GameContext context = gameParserService.parseLines(rawMatchData.getLines());
                GameResult result = gamePlayingService.processMatch(context, rawMatchData.getRank());

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
                modelAndView.addObject("turnInfos", turnInfos);

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
