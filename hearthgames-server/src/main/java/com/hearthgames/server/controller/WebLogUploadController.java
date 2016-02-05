package com.hearthgames.server.controller;

import com.hearthgames.server.config.security.UserInfo;
import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.hsreplay.HSReplayHandler;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
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
                String fileAsString = new String(bytes);

                if (rawLogProcessingService.isHSReplayFile(fileAsString)) {
                    processHSReplayFile(modelAndView, fileAsString);
                } else if (!rawLogProcessingService.doesLogFileContainAllLoggers(fileAsString)) {
                    modelAndView.setViewName("invalidlog");
                    return modelAndView;
                } else {
                    processLogFile(modelAndView, fileAsString);
                }

            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                modelAndView.addObject("error", "An error has occured.");
            }
        }
        return modelAndView;
    }

    private void processHSReplayFile(ModelAndView modelAndView, String xml) throws ParserConfigurationException, SAXException {

        List<RawGameData> rawGameDatas = rawLogProcessingService.processHSReplayFile(xml);

        List<GamePlayed> gamesPlayed = new ArrayList<>();
        String gameAccountId = null;
        int gamesAlreadyUploaded = 0;

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        HSReplayHandler handler;
        for (RawGameData rawGameData : rawGameDatas) {
            handler = new HSReplayHandler();

            try {
                saxParser.parse(new InputSource(new StringReader(rawGameData.getXml())), handler);

                GameContext context = handler.getContext();
                GameResult result = gamePlayingService.processGame(context, null);

                GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, getUserInfo());
                GamePlayed sameGame = gameService.findSameGame(gamePlayed);
                if (sameGame == null) {
                    gameService.saveGamePlayed(gamePlayed, context, result, false);
                    gamesPlayed.add(gamePlayed);
                } else {
                    gamesAlreadyUploaded++;
                }
                gameAccountId = gamePlayed.getFriendlyGameAccountId();
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }

        }

        modelAndView.addObject("gameAccountId", gameAccountId);
        modelAndView.addObject("gamesAlreadyUploaded", gamesAlreadyUploaded);

        WrapperUtil.addGamesPlayed(modelAndView, gamesPlayed, true, 1);
    }

    private void processLogFile(ModelAndView modelAndView, String logfile) {
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

                GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, getUserInfo());
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
    }

    private UserInfo getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo = null;
        if (principal != null && principal instanceof UserInfo) {
            userInfo = (UserInfo) principal;
        }
        return userInfo;
    }
}
