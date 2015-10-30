package com.hearthlogs.server.service;

import com.hearthlogs.server.match.Match;
import com.hearthlogs.server.match.Stats;
import com.hearthlogs.server.repository.MatchRepository;
import com.hearthlogs.server.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MatchPersistenceService {

    private StatsRepository statsRepository;
    private MatchRepository matchRepository;

    @Autowired
    public MatchPersistenceService(StatsRepository statsRepository, MatchRepository matchRepository) {
        this.statsRepository = statsRepository;
        this.matchRepository = matchRepository;
    }

    public Match saveRawPlayedGame(byte[] rawData, long startTime, long endTime, String rank) {
        Match match = new Match();
        match.setDateCreated(new Timestamp(System.currentTimeMillis()));
        match.setRawGame(rawData);
        match.setStartTime(new Timestamp(startTime));
        match.setEndTime(new Timestamp(endTime));
        match.setRank(rank);
//        matchRepository.save(match);
        return match;
    }

    public void indexGame(Stats stats) {
//        statsRepository.save(stats);
    }


}
