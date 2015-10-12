package com.hearthlogs.server.controller;

import com.hearthlogs.server.controller.request.RecordMatchRequest;
import com.hearthlogs.server.controller.response.RecordMatchResponse;
import com.hearthlogs.server.match.Match;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.Stats;
import com.hearthlogs.server.service.match.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestController
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private Environment env;

    @Autowired
    private MatchService matchService;

    @RequestMapping(value = "/upload", method=RequestMethod.POST)
    public ResponseEntity<RecordMatchResponse> uploadMatch(RecordMatchRequest request) {
        Stats stats;
        try {
            MatchContext context = matchService.deserializeGame(request.getData());
            if (!matchService.isMatchValid(context)) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Match match = matchService.saveRawPlayedGame(request.getData(), request.getStartTime(), request.getEndTime(), request.getRank());
            stats = matchService.processMatch(context, match);
            if (stats == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            matchService.indexGame(stats);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        RecordMatchResponse response = new RecordMatchResponse();
        response.setUrl(env.getProperty("match.url")+ stats.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}