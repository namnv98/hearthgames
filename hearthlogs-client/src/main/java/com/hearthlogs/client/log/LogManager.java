package com.hearthlogs.client.log;

import com.hearthlogs.client.match.MatchData;
import com.hearthlogs.client.match.event.RetryMatchRecordedEvent;
import com.hearthlogs.client.log.listener.LogListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Collection;

@Component
public class LogManager {

    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);

    private LogListener logListener;
    private ApplicationEventPublisher publisher;
    private File logFile;

    @Autowired
    public LogManager(LogListener logListener,
                      File logFile,
                      ApplicationEventPublisher publisher) {
        this.logListener = logListener;
        this.publisher = publisher;
        this.logFile = logFile;
    }

    public void start() throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        uploadCachedLogs();
        if (!logFile.exists()) {
            logger.error("Cannot find file : " + logFile.getName());
            return;
        }
        Tailer tailer = new Tailer(logFile, logListener, 1000, true);
        Thread thread = new Thread(tailer);
        thread.start();
    }

    private void uploadCachedLogs() {
        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("java.io.tmpdir")), new String[]{"chm"}, false);
        if (files.size() > 0) {
            logger.info("Found " + files.size() + " recorded game files that haven't been uploaded.");
        }
        for (File file: files) {
            try {
                byte[] data = FileUtils.readFileToByteArray(file);
                MatchData matchData = new MatchData();
                if (file.getName().startsWith("nonranked")) {
                    logger.info("Found Non Ranked match for upload : " + file.getName());
                    String[] gameInfo = file.getName().replace(".chm","").split("_");
                    matchData.setData(data);
                    matchData.setStartTime(Long.parseLong(gameInfo[1]));
                    matchData.setEndTime(Long.parseLong(gameInfo[2]));

                } else if (file.getName().startsWith("ranked")) {
                    logger.info("Found Ranked match for upload : " + file.getName());
                    String[] gameInfo = file.getName().replace(".chm","").split("_");
                    matchData.setData(data);
                    matchData.setStartTime(Long.parseLong(gameInfo[1]));
                    matchData.setEndTime(Long.parseLong(gameInfo[2]));
                    matchData.setRank(gameInfo[3]);
                }
                publisher.publishEvent(new RetryMatchRecordedEvent(this, matchData, file));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Failed to read file : " + file.getName());
            }
        }
    }
}