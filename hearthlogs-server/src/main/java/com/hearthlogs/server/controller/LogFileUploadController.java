package com.hearthlogs.server.controller;

import com.hearthlogs.server.game.analysis.domain.*;
import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class LogFileUploadController {

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameAnalysisService gameAnalysisService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("game");

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String logfile = new String(bytes);
                String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
                String[] lines = logfile.split(splitStr);

                List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
                modelAndView.addObject("uploadedMatches", rawMatchDatas.size());

                for (RawMatchData rawMatchData : rawMatchDatas) {
                    GameContext context = gameParserService.parseLines(rawMatchData.getLines());
                    GameResult result = gamePlayingService.processMatch(context, rawMatchData.getRank());

                    GenericTable cardInfo = gameAnalysisService.getCardSummary(result, context);
                    VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
                    List<GenericTable> healthArmorInfos = gameAnalysisService.getHealthArmor(result, context);
                    List<GenericTable> boardControlInfos = gameAnalysisService.getBoardControl(result, context);
                    List<GenericTable> cardAdvantageInfos = gameAnalysisService.getCardAdvantage(result, context);
                    List<TurnInfo> turnInfos = gameAnalysisService.getTurnInfo(result, context);

                    GenericTable manaInfo = gameAnalysisService.getManaInfo(result, context);

                    modelAndView.addObject("cardInfos", Collections.singletonList(cardInfo));
                    modelAndView.addObject("versusInfo", versusInfo);
                    modelAndView.addObject("healthArmorInfos", healthArmorInfos);
                    modelAndView.addObject("manaInfos", Collections.singletonList(manaInfo));
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
                        webContext.setVariable("boardControlInfos", boardControlInfos);
                        webContext.setVariable("cardAdvantageInfos", cardAdvantageInfos);
                        webContext.setVariable("turnInfos", turnInfos);
                    }
                }

            } catch (Exception e) {
                modelAndView.addObject("error", "An error has occured.");
                e.printStackTrace();
            }
        }

        return modelAndView;
    }
}
