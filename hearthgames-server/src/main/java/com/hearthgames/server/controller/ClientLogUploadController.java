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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class RecordGameRequest implements Serializable {
        private static final long serialVersionUID = 1;

        private byte[] data;
        private String rank;
        private long startTime;
        private long endTime;

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
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
    public ResponseEntity<RecordGameResponse> clientUpload(@RequestBody RecordGameRequest request) {

        String logfile = GameCompressionUtils.decompressGameData(request.getData());
        String[] lines = logfile.split("\n");

        List<RawGameData> rawGameDatas = rawLogProcessingService.processLogFile(Arrays.asList(lines), true);

        for (RawGameData rawGameData : rawGameDatas) {
            try {
                GameContext context = gameParserService.parseLines(rawGameData.getLines());
                GameResult result = gamePlayingService.processGame(context, rawGameData.getRank());

                GamePlayed gamePlayed = gameService.createGamePlayed(rawGameData, context, result, null);
                gamePlayed.setStartTime(getDateTimeFromTimestamp(request.getStartTime()));
                gamePlayed.setEndTime(getDateTimeFromTimestamp(request.getEndTime()));
                try {
                    gamePlayed.setRank(Integer.parseInt(request.getRank()));
                } catch (NumberFormatException e) {
                    gamePlayed.setRank(null);
                }

                if (!gameService.hasGameBeenPlayed(gamePlayed)) {
                    gameService.saveGamePlayed(gamePlayed, context, result, true);

                    RecordGameResponse response = new RecordGameResponse();
                    response.setUrl("http://hearthgames.com/game/"+gamePlayed.getId());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone
                .getDefault().toZoneId());
    }
}