package com.hearthgames.server.controller;

import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.service.GameParserService;
import com.hearthgames.server.service.GamePlayingService;
import com.hearthgames.server.service.RawLogProcessingService;
import com.hearthgames.server.util.WrapperUtil;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebLogUploadController {

    private static final Logger logger = LoggerFactory.getLogger(WebLogUploadController.class);

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/webUpload", method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadedgames");

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String logfile = new String(bytes);
                if (!rawLogProcessingService.doesLogFileContainAllLoggers(logfile)) {
                    modelAndView.setViewName("invalidlog");
                    return modelAndView;
                }

                String splitStr = logfile.contains("\r\n") ? "\r\n" : "\n";
                String[] lines = logfile.split(splitStr);

                List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines));

                List<GamePlayed> gamesPlayed = new ArrayList<>();
                String gameAccountId = null;
                int gamesAlreadyUploaded = 0;

                for (RawGameData rawGameData : rawGameDatas) {
                    try {
                        GameContext context = gameParserService.parseLines(rawGameData.getLines());
                        GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        UserInfo userInfo = null;
                        if (principal != null && principal instanceof UserInfo) {
                            userInfo = (UserInfo) principal;
                        }
                        GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, userInfo);
                        GamePlayed sameGame = gameService.findSameGame(gamePlayed);
                        if (sameGame == null) {
                            gameService.saveGamePlayed(gamePlayed, context, result, true);
                            gamesPlayed.add(gamePlayed);
                        } else {
                            gamesAlreadyUploaded++;
                        }
                        gameAccountId = gamePlayed.getFriendlyGameAccountId();

                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                        LocalDateTime now = LocalDateTime.now();
                        gameService.saveRawGameError(rawGameData, now, now);
                    }
                }

                modelAndView.addObject("gameAccountId", gameAccountId);
                modelAndView.addObject("gamesAlreadyUploaded", gamesAlreadyUploaded);

                WrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, true, 1);

            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                modelAndView.addObject("error", "An error has occured.");
            }
        }
        return modelAndView;
    }
}
