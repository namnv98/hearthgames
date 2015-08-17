package com.hearthlogs.client.match;

import com.hearthlogs.client.match.event.MatchRecordedEvent;
import com.hearthlogs.client.match.event.SaveMatchLocallyEvent;
import com.hearthlogs.client.log.event.LineReadEvent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;

@Component
public class MatchRecorder {

    private static final Logger logger = LoggerFactory.getLogger(MatchRecorder.class);

    private static final Pattern medalRankPattern = Pattern.compile("name=Medal_Ranked_(.*) family");
    private static final String MEDAL_RANKED = "unloading name=Medal_Ranked";
    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String GAME_STATE_COMPLETE = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
    private static final String REGISTER_FRIEND_CHALLENGE = "---RegisterFriendChallenge---";

    @Autowired
    private ApplicationEventPublisher publisher;

    private StringBuilder currentMatch;
    private boolean matchComplete;
    private String rank;
    private long startTime;
    private long endTime;

    private String lastRecordedRank;

    @EventListener
    public void handleLine(LineReadEvent event) {
        String line = event.getLine().trim();
        if (line.startsWith(CREATE_GAME)) {
            matchComplete = false;
            rank = null;
            startTime = System.currentTimeMillis();
            logger.info("A Game has started.  Game logging has begun.");
            currentMatch = new StringBuilder();
            currentMatch.append(event.getLine()).append("\n");
        } else if (currentMatch != null && line.startsWith(GAME_STATE_COMPLETE)) {
            currentMatch.append(event.getLine()).append("\n");
            matchComplete = true;
            endTime = System.currentTimeMillis();
        } else if (currentMatch != null && matchComplete && line.startsWith(MEDAL_RANKED)) {
            rank = getRank(line);
        } else if (currentMatch != null && matchComplete && line.startsWith(REGISTER_FRIEND_CHALLENGE)) {
            logger.info("The Game is over.  Attempting to record to HPT web service.");
            MatchData matchData = new MatchData();
            matchData.setData(compress(currentMatch.toString()));
            matchData.setStartTime(startTime);
            matchData.setEndTime(endTime);

            // sometimes the rank of another player shows up in the log. so we check if the difference
            // between the 2 ranks we detect is more than 1.  If it is than something is wrong since you can go up 2 ranks
            // from 1 match played.  This is a best approximation at the moment but seems good enough.
            if (rank != null && lastRecordedRank != null && Math.abs(Integer.valueOf(rank) - Integer.valueOf(lastRecordedRank)) > 1) {
                rank = lastRecordedRank;
            }
            matchData.setRank(rank);

            currentMatch = null;
            lastRecordedRank = rank;
            publisher.publishEvent(new MatchRecordedEvent(this, matchData));
        } else if (currentMatch != null && event.isLoggable()) {
            currentMatch.append(event.getLine()).append("\n");
        }
    }

    private String getRank(String line) {
        String rank = null;
        Matcher matcher = medalRankPattern.matcher(line);
        if (matcher.find()) {
            rank = matcher.group(1);
        }
        return rank;
    }

    @EventListener
    public void handleData(SaveMatchLocallyEvent event) {
        saveGameToFile(event.getData());
    }

    private void saveGameToFile(MatchData matchData) {
        String fileName = System.getProperty("java.io.tmpdir");
        if (rank != null) {
            fileName += "ranked_"+startTime+"_"+endTime+"_" + rank + ".chm";
        } else {
            fileName += "nonranked_"+startTime+"_"+endTime+".chm";

        }
        File file = new File(fileName);
        logger.info("Saving match to : " + fileName);
        try {
            FileUtils.writeByteArrayToFile(file, matchData.getData());
        } catch (IOException e) {
            logger.error("Error saving match to : " + fileName);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
        }
    }

    private byte[] compress(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream out = new DeflaterOutputStream(baos)){
            out.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }
}