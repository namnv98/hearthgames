package com.hearthlogs.web.service.match;

import com.hearthlogs.web.domain.*;
import com.hearthlogs.web.log.parser.MatchActivityParser;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.match.RawMatch;
import com.hearthlogs.web.repository.jpa.RawMatchRepository;
import com.hearthlogs.web.repository.solr.CompletedMatchRepository;
import com.hearthlogs.web.service.CardService;
import com.hearthlogs.web.service.match.handler.ActivityHandlers;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.InflaterInputStream;

@Component
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private CardService cardService;
    private CompletedMatchRepository completedMatchRepository;
    private RawMatchRepository rawMatchRepository;
    private MatchActivityParser matchActivityParser;
    private ActivityHandlers activityHandlers;

    @Autowired
    public MatchService(CardService cardService,
                        MatchActivityParser matchActivityParser,
                        CompletedMatchRepository completedMatchRepository,
                        RawMatchRepository rawMatchRepository,
                        ActivityHandlers activityHandlers) {
        this.cardService = cardService;
        this.matchActivityParser = matchActivityParser;
        this.completedMatchRepository = completedMatchRepository;
        this.rawMatchRepository = rawMatchRepository;
        this.activityHandlers = activityHandlers;
    }

    public MatchContext deserializeGame(byte[] rawData) {
        MatchContext context = null;
        try {
            String game = decompressGameData(rawData);

            String[] lines = game.split("\n");

            context = new MatchContext();
            for (String line : lines) {
                matchActivityParser.parse(context, line);
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error(sw.toString());
        }
        return context;
    }

    public RawMatch saveRawPlayedGame(byte[] rawData, long startTime, long endTime, String rank) {
        RawMatch rawMatch = new RawMatch();
        rawMatch.setDateCreated(new Timestamp(System.currentTimeMillis()));
        rawMatch.setRawGame(rawData);
        rawMatch.setStartTime(new Timestamp(startTime));
        rawMatch.setEndTime(new Timestamp(endTime));
        rawMatch.setRank(rank);
        rawMatchRepository.save(rawMatch);
        return rawMatch;
    }

    public CompletedMatch processGame(MatchContext context, RawMatch rawMatch) {

        for (Activity activity: context.getActivities()) {
            handle(context, activity);
        }

        CompletedMatch completedMatch = context.getCompletedMatch();
        completedMatch.setId(rawMatch.getId()+"");
        completedMatch.setFriendlyPlayer(context.getFriendlyPlayer().getName());
        completedMatch.setOpposingPlayer(context.getOpposingPlayer().getName());
        completedMatch.setFriendlyAccountId(context.getFriendlyPlayer().getGameAccountIdLo());
        completedMatch.setOpposingAccountId(context.getOpposingPlayer().getGameAccountIdLo());
        completedMatch.setStartTime(rawMatch.getStartTime());
        completedMatch.setEndTime(rawMatch.getEndTime());
        completedMatch.setRank(rawMatch.getRank());

        Card card = (Card) context.getEntityById(context.getFriendlyPlayer().getHeroEntity());
        CardDetails cardDetails = cardService.getCardDetails(card.getCardid());
        completedMatch.setFriendlyClass(cardDetails.getPlayerClass());

        card = (Card) context.getEntityById(context.getOpposingPlayer().getHeroEntity());
        cardDetails = cardService.getCardDetails(card.getCardid());
        completedMatch.setOpposingClass(cardDetails.getPlayerClass());

        if ("1".equals(context.getFriendlyPlayer().getFirstPlayer())) {
            completedMatch.setFirstPlayer(context.getFriendlyPlayer().getName());
        } else {
            completedMatch.setFirstPlayer(context.getOpposingPlayer().getName());
        }

        return completedMatch;
    }

    public void indexGame(CompletedMatch completedMatch) {
        completedMatchRepository.save(completedMatch);
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
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
