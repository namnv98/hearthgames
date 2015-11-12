package com.hearthlogs.server.controller;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.stats.domain.MatchStatistics;
import com.hearthlogs.server.match.raw.domain.RawMatchData;
import com.hearthlogs.server.match.view.domain.HealthArmorSummary;
import com.hearthlogs.server.match.view.domain.VersusInfo;
import com.hearthlogs.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
    private MatchStatisticalAnalysisService matchStatisticalAnalysisService;

    @Autowired
    private MatchResultRenderingService matchResultRenderingService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("match");

        List<MatchStatistics> statisticsList = new ArrayList<>();
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
                    MatchStatistics matchStatistics = matchStatisticalAnalysisService.calculateStatistics(matchResult, context);
                    statisticsList.add(matchStatistics);

                    VersusInfo versusInfo = matchResultRenderingService.getVersusInfo(matchResult, context);
                    HealthArmorSummary healthArmorSummary = matchResultRenderingService.getHealthSummary(matchResult, context);

                    modelAndView.addObject("versusInfo", versusInfo);
                    modelAndView.addObject("healthArmorSummary", healthArmorSummary);
                    modelAndView.addObject("manaStats", matchStatistics);
                }

            } catch (Exception e) {
                modelAndView.addObject("error", "An error has occured.");
                e.printStackTrace();
            }
        }
        modelAndView.addObject("statsList", statisticsList);

        return modelAndView;
    }
}
