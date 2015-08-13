package com.hearthlogs.web.controller;

import com.hearthlogs.api.ws.request.RecordMatchRequest;
import com.hearthlogs.api.ws.response.RecordMatchResponse;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.match.RawMatch;
import com.hearthlogs.web.service.MatchService;
import com.hearthlogs.web.match.CompletedMatch;
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
@RequestMapping("/upload")
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private Environment env;

    @Autowired
    private MatchService matchService;

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<RecordMatchResponse> uploadGame(@RequestBody RecordMatchRequest request) {
        CompletedMatch completedMatch;
        try {
            MatchContext context = matchService.deserializeGame(request.getData());
            if ("0".equals(context.getFriendlyPlayer().getGameAccountIdLo())) { // We found a match that was played against the computer.  This is not acceptable.
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            RawMatch rawMatch = matchService.saveRawPlayedGame(request.getData(), request.getStartTime(), request.getEndTime(), request.getRank());

            completedMatch = matchService.processGame(context, rawMatch);
            if (completedMatch == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            matchService.indexGame(completedMatch);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        RecordMatchResponse response = new RecordMatchResponse();
        response.setUrl(env.getProperty("matchUrl")+completedMatch.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}