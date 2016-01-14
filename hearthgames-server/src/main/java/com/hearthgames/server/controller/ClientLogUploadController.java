package com.hearthgames.server.controller;

import com.hearthgames.server.database.domain.GamePlayed;
import com.hearthgames.server.database.service.GameService;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.service.GameParserService;
import com.hearthgames.server.service.GamePlayingService;
import com.hearthgames.server.service.RawLogProcessingService;
import com.hearthgames.server.util.GameCompressionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@RestController
public class ClientLogUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ClientLogUploadController.class);

    @Autowired
    private RawLogProcessingService rawLogProcessingService;

    @Autowired
    private GameParserService gameParserService;

    @Autowired
    private GamePlayingService gamePlayingService;

    @Autowired
    private GameService gameService;

    public static class RecordGameResponse implements Serializable {
        private static final long serialVersionUID = 1;

        private String url;
        private String msg;
        private boolean upgradeRequired;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isUpgradeRequired() {
            return upgradeRequired;
        }

        public void setUpgradeRequired(boolean upgradeRequired) {
            this.upgradeRequired = upgradeRequired;
        }
    }

    public static class RecordGameRequest implements Serializable {
        private static final long serialVersionUID = 1;

        private int version;
        private int gameType;
        private byte[] data;
        private long startTime;
        private long endTime;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getGameType() {
            return gameType;
        }

        public void setGameType(int gameType) {
            this.gameType = gameType;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }

    @RequestMapping(value = "/clientUpload", method = RequestMethod.POST)
    public ResponseEntity<RecordGameResponse> clientUpload(@RequestBody RecordGameRequest request)  {

        if (request.getVersion() == 0) {
            RecordGameResponse response = new RecordGameResponse();
            response.setUpgradeRequired(true);
            response.setMsg("A new version of the client is required to upload games.  Please go to http://hearthgames.com/download to get the new one.");
            // TODO change this in a future release...the first release didn't support messages
            response.setUrl("A new version of the client is required to upload games.  Please go to http://hearthgames.com/download to get the new one.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (request.getVersion() == 1) {
            RecordGameResponse response = new RecordGameResponse();
            response.setUpgradeRequired(true);
            response.setMsg("A new version of the client is required to upload games.  Please go to http://hearthgames.com/download to get the new one.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String logfile = GameCompressionUtils.decompressGameData(request.getData());

        if (rawLogProcessingService.doesLogFileContainAllLoggers(logfile)) {
            String[] lines = logfile.split("\n");

            List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines), request.getGameType());

            if (!CollectionUtils.isEmpty(rawGameDatas)) {
                RawGameData rawGameData = rawGameDatas.get(0);

                try {
                    GameContext context = gameParserService.parseLines(rawGameData.getLines());
                    GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                    GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);
                    gamePlayed.setStartTime(getDateTimeFromTimestamp(request.getStartTime()));
                    gamePlayed.setEndTime(getDateTimeFromTimestamp(request.getEndTime()));
                    gamePlayed.setRank(rawGameData.getRank());

                    GamePlayed sameGame = gameService.findSameGame(gamePlayed);
                    if (sameGame == null) {
                        gameService.saveGamePlayed(gamePlayed, context, result, true);
                    }
                    Long gameId = sameGame != null ? sameGame.getId() : gamePlayed.getId();
                    RecordGameResponse response = new RecordGameResponse();
                    response.setUrl("http://hearthgames.com/game/"+gameId);
                    return new ResponseEntity<>(response, HttpStatus.OK);

                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                    gameService.saveRawGameError(rawGameData, getDateTimeFromTimestamp(request.getStartTime()), getDateTimeFromTimestamp(request.getEndTime()));
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone
                .getDefault().toZoneId());
    }
}