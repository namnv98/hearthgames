package com.hearthlogs.server.controller;

import com.hearthlogs.server.config.security.UserInfo;
import com.hearthlogs.server.database.domain.GamePlayed;
import com.hearthlogs.server.database.service.GameService;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.log.domain.RawMatchData;
import com.hearthlogs.server.service.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LogFileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(LogFileUploadController.class);

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        String viewName = null;

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String logfile = new String(bytes);
                String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
                String[] lines = logfile.split(splitStr);

                List<RawMatchData> rawMatchDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));

                for (RawMatchData rawMatchData : rawMatchDatas) {
                    try {
                        GameContext context = gameParserService.parseLines(rawMatchData.getLines());
                        GameResult result = gamePlayingService.processGame(context, rawMatchData.getRank());

                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        UserInfo userInfo = null;
                        if (principal != null && principal instanceof UserInfo) {
                            userInfo = (UserInfo) principal;
                        }
                        GamePlayed gamePlayed = gameService.createGamePlayed(rawMatchData, context, result, userInfo);
                        if (!gameService.hasGameBeenPlayed(gamePlayed)) {
                            gameService.saveGamePlayed(gamePlayed);
                        }
                        viewName = "redirect:/account/"+gamePlayed.getFriendlyGameAccountId()+"/games";

                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                        gameService.saveRawMatchError(rawMatchData);
                    }
                }

            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                modelAndView.addObject("error", "An error has occured.");
            }
        }
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
