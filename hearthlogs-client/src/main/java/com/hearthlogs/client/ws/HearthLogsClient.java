package com.hearthlogs.client.ws;

import com.hearthlogs.api.ws.request.RecordMatchRequest;
import com.hearthlogs.api.ws.response.RecordMatchResponse;
import com.hearthlogs.client.match.event.SaveMatchLocallyEvent;
import com.hearthlogs.client.config.ApplicationProperties;
import com.hearthlogs.client.match.MatchData;
import com.hearthlogs.client.match.event.MatchRecordedEvent;
import com.hearthlogs.client.match.event.RetryMatchRecordedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
public class HearthLogsClient {

    private static final Logger logger = LoggerFactory.getLogger(HearthLogsClient.class);

    private RestTemplate restTemplate;
    private ApplicationProperties properties;
    private ApplicationEventPublisher publisher;

    @Autowired
    public HearthLogsClient(RestTemplate restTemplate,
                            ApplicationProperties properties,
                            ApplicationEventPublisher publisher) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.publisher = publisher;
    }

    @EventListener
    public void handleMatchRecorded(MatchRecordedEvent event) {
        recordMatch(event.getData());
    }

    @EventListener
    public void handleRetryMatchRecorded(RetryMatchRecordedEvent event) {
        retryRecordMatch(event.getData(), event.getFile());
    }

    private RecordMatchRequest createRequestFromData(MatchData data) {
        RecordMatchRequest request = new RecordMatchRequest();
        request.setData(data.getData());
        request.setRank(data.getRank());
        request.setStartTime(data.getStartTime());
        request.setEndTime(data.getEndTime());
        return request;
    }

    private void retryRecordMatch(MatchData matchData, File file) {
        ResponseEntity<RecordMatchResponse> response = postMatchToServer(createRequestFromData(matchData));
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            logger.info("Game recorded and available for viewing at: " + response.getBody().getUrl());
            deleteFile(file);
        } else if (response != null && response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
            logger.info("The game type is not recordable. Some how a non-play mode game was uploaded.");
        } else if (response == null) {
            logger.info("Will try to save again on restart of the client.");
        }
    }

    private void recordMatch(MatchData matchData) {
        ResponseEntity<RecordMatchResponse> response = postMatchToServer(createRequestFromData(matchData));
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            logger.info("Game recorded and available for viewing at: " + response.getBody().getUrl());
        } else if (response != null && response.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
            logger.info("The game type is not recordable. Somehow a non-play mode game was uploaded.");
        } else if (response == null){
            logger.info("Attempting to save match to local cache for later upload, on restart of the client.");
            publisher.publishEvent(new SaveMatchLocallyEvent(this, matchData));
        }
    }

    private ResponseEntity<RecordMatchResponse> postMatchToServer(RecordMatchRequest request) {
        if (request.getRank() == null) {
            logger.info("Posting Non-Ranked Play Mode match to the server...");
        } else {
            logger.info("Posting Ranked Play Mode match to the server");
            logger.info("Rank detected : " + request.getRank());
        }
        ResponseEntity<RecordMatchResponse> response = null;
        try {
            response = restTemplate.postForEntity(this.properties.getUploadUrl(), request, RecordMatchResponse.class);
        } catch (Exception e) {
            logger.info("HPT web service not available.");
        }
        return response;
    }

    private void deleteFile(File file) {
        try {
            logger.info("Attempting to delete temporary file: " + file.getAbsolutePath());
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("Deleted temporary file : " + file.getAbsolutePath());
            } else {
                logger.error("Failed to delete temporary file : " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to delete temporary file : " + file.getAbsolutePath());
        }
    }
}