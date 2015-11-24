package com.hearthlogs.server.controller;

import com.hearthlogs.server.config.security.UserInfo;
import com.hearthlogs.server.database.service.GamePlayedService;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')")
public class LogFileUploadController {

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GamePlayedService gamePlayedService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/games");

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String logfile = new String(bytes);
                String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
                String[] lines = logfile.split(splitStr);

                List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));

                for (RawMatchData rawMatchData : rawMatchDatas) {
                    GameContext context = gameParserService.parseLines(rawMatchData.getLines());
                    GameResult result = gamePlayingService.processMatch(context, rawMatchData.getRank());

                    if (!gamePlayedService.hasGameBeenPlayed(rawMatchData, context)) {
                        gamePlayedService.createGamePlayed(rawMatchData, context, result, userInfo);
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
