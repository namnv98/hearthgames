package com.hearthlogs.server.controller;

import com.hearthlogs.server.match.analysis.domain.CardInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.analysis.domain.ManaInfo;
import com.hearthlogs.server.match.log.domain.RawMatchData;
import com.hearthlogs.server.match.analysis.domain.HealthArmorInfo;
import com.hearthlogs.server.match.analysis.domain.VersusInfo;
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
    private MatchParserService matchParserService;

    @Autowired
    private MatchPlayingService matchPlayingService;

    @Autowired
    private MatchAnalysisService matchAnalysisService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("match");

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String logfile = new String(bytes);
                String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
                String[] lines = logfile.split(splitStr);

                List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));
                modelAndView.addObject("uploadedMatches", rawMatchDatas.size());

                for (RawMatchData rawMatchData : rawMatchDatas) {
                    ParseContext context = matchParserService.parseLines(rawMatchData.getLines());
                    MatchResult matchResult = matchPlayingService.processMatch(context, rawMatchData.getRank());

                    CardInfo cardInfo = matchAnalysisService.getCardInfo(matchResult, context);
                    VersusInfo versusInfo = matchAnalysisService.getVersusInfo(matchResult, context);
                    List<HealthArmorInfo> healthArmorInfos = matchAnalysisService.getHealthArmorInfo(matchResult, context);

                    ManaInfo manaInfo = matchAnalysisService.getManaInfo(matchResult, context);

                    modelAndView.addObject("cardInfo", cardInfo);
                    modelAndView.addObject("versusInfo", versusInfo);
                    modelAndView.addObject("healthArmorInfos", healthArmorInfos);
                    modelAndView.addObject("manaStats", manaInfo);
                }

            } catch (Exception e) {
                modelAndView.addObject("error", "An error has occured.");
                e.printStackTrace();
            }
        }

        return modelAndView;
    }
}
