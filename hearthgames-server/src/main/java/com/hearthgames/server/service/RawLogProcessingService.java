package com.hearthgames.server.service;

import com.hearthgames.server.game.log.domain.FilteredLineData;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.log.domain.RawGameData;
import com.hearthgames.server.game.log.filter.AssetLineFilter;
import com.hearthgames.server.game.log.filter.BobLineFilter;
import com.hearthgames.server.game.log.filter.PowerLineFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RawLogProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RawLogProcessingService.class);

    private static final Pattern medalRankPattern = Pattern.compile("name=Medal_Ranked_(.*) family");
    private static final String MEDAL_RANKED = "unloading name=Medal_Ranked";
    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String GAME_STATE_COMPLETE = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
    private static final String REGISTER_FRIEND_CHALLENGE = "---RegisterFriendChallenge---";

    private PowerLineFilter powerLineFilter;
    private BobLineFilter bobLineFilter;
    private AssetLineFilter assetLineFilter;

    @Autowired
    public RawLogProcessingService(PowerLineFilter powerLineFilter,
                                   BobLineFilter bobLineFilter,
                                   AssetLineFilter assetLineFilter) {
        this.powerLineFilter = powerLineFilter;
        this.bobLineFilter = bobLineFilter;
        this.assetLineFilter = assetLineFilter;
    }

    public List<RawGameData> processLogFile(List<String> lines, boolean prefiltered) {

        List<RawGameData> rawGameDatas = new ArrayList<>();

        boolean gameComplete = false;
        List<LogLineData> currentGame = null;
        List<String> currentRawGame = null;
        Integer rank = null;
        for (String rawLine: lines) {

            String timestamp = "";
            String lineWithoutTimestamp;
            FilteredLineData filteredLineData;
            if (rawLine.contains(": [")) {
                timestamp = rawLine.substring(0, rawLine.indexOf(": ["));
                lineWithoutTimestamp = rawLine.substring(rawLine.indexOf("["));
                filteredLineData = filterLine(lineWithoutTimestamp);
            } else if (prefiltered) {
                filteredLineData = new FilteredLineData(rawLine, true);
            } else {
                filteredLineData = filterLine(rawLine); // For games that don't have timestamps
            }

            if (filteredLineData != null) {
                String line = filteredLineData.getLine();

                if (line.startsWith(CREATE_GAME)) {
                    gameComplete = false;
                    rank = null;
                    currentGame = new ArrayList<>();
                    currentRawGame = new ArrayList<>();
                    LogLineData data = new LogLineData(timestamp, line);
                    currentGame.add(data);
                    currentRawGame.add(rawLine);
                } else if (currentGame != null && line.startsWith(GAME_STATE_COMPLETE)) {
                    LogLineData data = new LogLineData(timestamp, line);
                    currentGame.add(data);
                    currentRawGame.add(rawLine);
                    gameComplete = true;
                } else if (currentGame != null && gameComplete && line.startsWith(MEDAL_RANKED)) {
                    int rankFound = getRank(line);
                    if (rank == null || rankFound < rank) {
                        rank = rankFound;
                    }
                    currentRawGame.add(rawLine);
                } else if (currentGame != null && gameComplete && (line.startsWith(REGISTER_FRIEND_CHALLENGE) || prefiltered)) {
                    currentRawGame.add(rawLine);
                    RawGameData rawGameData = new RawGameData();
                    rawGameData.setLines(currentGame);
                    rawGameData.setRawLines(currentRawGame);
                    rawGameData.setRank(rank);
                    rawGameDatas.add(rawGameData);
                    if (prefiltered) {
                        return rawGameDatas;
                    }
                } else if (currentGame != null && filteredLineData.isLoggable()) {
                    LogLineData data = new LogLineData(timestamp, line);
                    currentGame.add(data);
                    currentRawGame.add(rawLine);
                }
            }
        }

        return rawGameDatas;
    }

    private FilteredLineData filterLine(String line) {
        FilteredLineData filteredLineData = null;
        String filteredLine = powerLineFilter.filter(line);
        if (filteredLine != null) {
            filteredLineData = new FilteredLineData(filteredLine, true);
        }
        filteredLine = assetLineFilter.filter(line);
        if (filteredLine != null) {
            filteredLineData = new FilteredLineData(filteredLine, false);
        }
        filteredLine = bobLineFilter.filter(line);
        if (filteredLine != null) {
            filteredLineData = new FilteredLineData(filteredLine, false);
        }
        return filteredLineData;
    }

    private int getRank(String line) {
        int rank = 0;
        Matcher matcher = medalRankPattern.matcher(line);
        if (matcher.find()) {
            String r = null;
            try {
                r = matcher.group(1);
                rank = Integer.parseInt(r);
            } catch (NumberFormatException e) {
                logger.warn("Found a rank that was not parseable : " + r);
            }
        }
        return rank;
    }

}
