package com.hearthlogs.server.service.match;

import com.hearthlogs.server.domain.Card;
import com.hearthlogs.server.domain.CardDetails;
import com.hearthlogs.server.log.parser.MatchActivityParser;
import com.hearthlogs.server.match.Activity;
import com.hearthlogs.server.match.Match;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.Stats;
import com.hearthlogs.server.repository.jpa.MatchRepository;
import com.hearthlogs.server.repository.solr.StatsRepository;
import com.hearthlogs.server.service.match.handler.ActivityHandlers;
import com.hearthlogs.server.service.CardService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.InflaterInputStream;

@Component
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private CardService cardService;
    private StatsRepository statsRepository;
    private MatchRepository matchRepository;
    private MatchActivityParser matchActivityParser;
    private ActivityHandlers activityHandlers;

    @Autowired
    public MatchService(CardService cardService,
                        MatchActivityParser matchActivityParser,
                        StatsRepository statsRepository,
                        MatchRepository matchRepository,
                        ActivityHandlers activityHandlers) {
        this.cardService = cardService;
        this.matchActivityParser = matchActivityParser;
        this.statsRepository = statsRepository;
        this.matchRepository = matchRepository;
        this.activityHandlers = activityHandlers;
    }

    public MatchContext deserializeGame(byte[] rawData) {
        MatchContext context = new MatchContext();
        String game = decompressGameData(rawData);
        String[] lines = game.split("\n");
        String currentLine = null;
        try {
            for (String line : lines) {
                currentLine = line;
                matchActivityParser.parse(context, line.trim());
            }
        } catch (Exception e) {
            long time = System.currentTimeMillis();
            File file = new File(System.getProperty("java.io.tmpdir")+time+".log");
            logger.error("Failed on line " + currentLine);
            logger.error("Writing game file: " + file.getAbsolutePath());
            try {
                FileUtils.writeStringToFile(file, game, "UTF-8");
            } catch (IOException e1) {
                logger.error("Failed to write game file: " + time+".log");
            }
            throw e;
        }
        return context;
    }

    public boolean isMatchValid(MatchContext context) {
        // We found a match that was played against the computer.  This is not acceptable.
        return !"0".equals(context.getOpposingPlayer().getGameAccountIdLo());
    }

    public Match saveRawPlayedGame(byte[] rawData, long startTime, long endTime, String rank) {
        Match match = new Match();
        match.setDateCreated(new Timestamp(System.currentTimeMillis()));
        match.setRawGame(rawData);
        match.setStartTime(new Timestamp(startTime));
        match.setEndTime(new Timestamp(endTime));
        match.setRank(rank);
        matchRepository.save(match);
        return match;
    }

    public Stats processMatch(MatchContext context, Match match) {

        for (Activity activity: context.getActivities()) {
            handle(context, activity);
        }

        Stats stats = new Stats();
        stats.setId(match.getId()+"");
//        stats.setFriendlyPlayer(context.getFriendlyPlayer().getName());
//        stats.setOpposingPlayer(context.getOpposingPlayer().getName());
//        stats.setFriendlyAccountId(context.getFriendlyPlayer().getGameAccountIdLo());
//        stats.setOpposingAccountId(context.getOpposingPlayer().getGameAccountIdLo());
//        stats.setStartTime(match.getStartTime());
//        stats.setEndTime(match.getEndTime());
        stats.setRank(match.getRank());

        Card card = (Card) context.getEntityById(context.getWinner().getHeroEntity());
        CardDetails cardDetails = cardService.getCardDetails(card.getCardid());
        stats.setWinnerClass(cardDetails.getPlayerClass());

        card = (Card) context.getEntityById(context.getLoser().getHeroEntity());
        cardDetails = cardService.getCardDetails(card.getCardid());
        stats.setLoserClass(cardDetails.getPlayerClass());

//        stats.setWinner(context.getWinner().getName());
//        stats.setLoser(context.getLoser().getName());
//        stats.setQuitter(context.getQuitter() != null ? context.getQuitter().getName() : null);
//        stats.setFirstPlayer(context.getFirst().getName());

        List<String> winnerCards = new ArrayList<>();
//        List<String>
        List<Card> winningCards;
        if (context.getFriendlyPlayer() == context.getWinner()) {
            winningCards = context.getFriendlyCards();
            winningCards.addAll(context.getFriendlyStartingCards());



        } else {
            winningCards = context.getOpposingCards();
        }

//        completedMatch.setWinnerCards();




        return stats;
    }

    public void indexGame(Stats stats) {
        statsRepository.save(stats);
    }


    public void handle(MatchContext context, Activity activity) {
        // Handler the action first then any child actions. The reason for this is that the action contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] SubType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(context, activity);
        doChildActivities(context, activity.getChildren());
    }

    private void doChildActivities(MatchContext context, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                doChildActivities(context, activity.getChildren());
                doActivity(context, activity);
            }
        }
    }

    private void doActivity(MatchContext context, Activity activity) {
        playActivity(context, activity);
        updateGameActivityData(context, activity);
    }

    private void playActivity(MatchContext context, Activity activity) {
        activityHandlers.processActivity(context, activity);
    }

    private void updateGameActivityData(MatchContext context, Activity activity) {
        if (activity.getType().equals(Activity.Type.TAG_CHANGE)) {
            copyNonNullProperties(activity.getEntity(), context.getEntityById(activity.getEntityId()));
        }
    }

    public String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public String decompressGameData(byte[] data) {
        try (
                InputStream is = new ByteArrayInputStream(data);
                InflaterInputStream iis = new InflaterInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(512)
        ) {
            int b;
            while ((b = iis.read()) != -1) {
                baos.write(b);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
