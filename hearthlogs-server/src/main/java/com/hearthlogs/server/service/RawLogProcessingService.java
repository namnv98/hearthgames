package com.hearthlogs.server.service;

import com.hearthlogs.server.match.log.domain.FilteredLineData;
import com.hearthlogs.server.match.log.domain.LogLineData;
import com.hearthlogs.server.match.log.domain.RawMatchData;
import com.hearthlogs.server.match.log.filter.AssetLineFilter;
import com.hearthlogs.server.match.log.filter.BobLineFilter;
import com.hearthlogs.server.match.log.filter.PowerLineFilter;
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

    public List<RawMatchData> processLogFile(List<String> lines) {

        List<RawMatchData> rawMatchDatas = new ArrayList<>();

        boolean matchComplete = false;
        List<LogLineData> currentMatch = null;
        Integer rank = null;
        for (String rawLine: lines) {

            String timestamp = "";
            String lineWithoutTimestamp = "";
            if (rawLine.contains(": [")) {
                timestamp = rawLine.substring(0, rawLine.indexOf(": ["));
                lineWithoutTimestamp = rawLine.substring(rawLine.indexOf("["));
            }
            FilteredLineData filteredLineData = filterLine(lineWithoutTimestamp);

            if (filteredLineData != null) {
                String line = filteredLineData.getLine();

                if (line.startsWith(CREATE_GAME)) {
                    matchComplete = false;
                    rank = null;
                    currentMatch = new ArrayList<>();
                    LogLineData data = new LogLineData(timestamp, line);
                    currentMatch.add(data);
                } else if (currentMatch != null && line.startsWith(GAME_STATE_COMPLETE)) {
                    LogLineData data = new LogLineData(timestamp, line);
                    currentMatch.add(data);
                    matchComplete = true;
                } else if (currentMatch != null && matchComplete && line.startsWith(MEDAL_RANKED)) {
                    int rankFound = getRank(line);
                    if (rank == null || rankFound < rank) {
                        rank = rankFound;
                    }
                } else if (currentMatch != null && matchComplete && line.startsWith(REGISTER_FRIEND_CHALLENGE)) {
                    RawMatchData rawMatchData = new RawMatchData();
                    rawMatchData.setLines(currentMatch);
                    rawMatchData.setRank(rank);
                    rawMatchDatas.add(rawMatchData);
                } else if (currentMatch != null && filteredLineData.isLoggable()) {
                    LogLineData data = new LogLineData(timestamp, line);
                    currentMatch.add(data);
                }
            }
        }

        return rawMatchDatas;
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
                logger.warn("Found a rank that was not parseable, maybe this is the Legend rank? : " + r);
            }
        }
        return rank;
    }

}
