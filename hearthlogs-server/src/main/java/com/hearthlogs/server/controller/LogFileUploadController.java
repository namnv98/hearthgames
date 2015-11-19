package com.hearthlogs.server.controller;

import com.hearthlogs.server.game.analysis.domain.*;
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

import java.util.Arrays;
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

                    CardInfo cardInfo = gameAnalysisService.getCardInfo(result, context);
                    VersusInfo versusInfo = gameAnalysisService.getVersusInfo(result, context);
                    List<HealthArmorInfo> healthArmorInfos = gameAnalysisService.getHealthArmorInfo(result, context);
                    List<BoardControlInfo> boardControlInfos = gameAnalysisService.getBoardControlInfo(result, context);
                    List<CardAdvantageInfo> cardAdvantageInfos = gameAnalysisService.getCardAdvantageInfo(result, context);

                    ManaInfo manaInfo = gameAnalysisService.getManaInfo(result, context);

                    modelAndView.addObject("cardInfo", cardInfo);
                    modelAndView.addObject("versusInfo", versusInfo);
                    modelAndView.addObject("healthArmorInfos", healthArmorInfos);
                    modelAndView.addObject("manaInfo", manaInfo);
                    modelAndView.addObject("boardControlInfos", boardControlInfos);
                    modelAndView.addObject("cardAdvantageInfos", cardAdvantageInfos);
                }

            } catch (Exception e) {
                modelAndView.addObject("error", "An error has occured.");
                e.printStackTrace();
            }
        }

        return modelAndView;
    }
}
