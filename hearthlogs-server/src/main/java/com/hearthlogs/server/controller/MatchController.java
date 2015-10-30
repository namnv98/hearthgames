package com.hearthlogs.server.controller;

import com.hearthlogs.server.controller.request.RecordMatchRequest;
import com.hearthlogs.server.controller.response.RecordMatchResponse;
import com.hearthlogs.server.match.Match;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import com.hearthlogs.server.match.Stats;
import com.hearthlogs.server.service.MatchParserService;
import com.hearthlogs.server.service.MatchPersistenceService;
import com.hearthlogs.server.service.MatchService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private Environment env;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchParserService matchParserService;

    @Autowired
    private MatchPersistenceService matchPersistenceService;

    @RequestMapping(value = "/upload", method=RequestMethod.POST)
    public ResponseEntity<RecordMatchResponse> uploadMatch(RecordMatchRequest request) {
        Stats stats;
        try {
            List<String> lines = matchParserService.deserializeGame(request.getData());
            MatchContext context = matchParserService.processMatch(lines);
            if (!context.isMatchValid()) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            MatchResult result = matchService.processMatch(context, request.getRank());
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Match match = matchPersistenceService.saveRawPlayedGame(request.getData(), request.getStartTime(), request.getEndTime(), request.getRank());
            stats = new Stats();
            matchPersistenceService.indexGame(stats);


        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        RecordMatchResponse response = new RecordMatchResponse();
        response.setUrl(env.getProperty("match.url")+ stats.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}