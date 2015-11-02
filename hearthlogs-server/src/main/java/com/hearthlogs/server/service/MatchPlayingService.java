package com.hearthlogs.server.service;

import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.play.handler.ActionHandler;
import com.hearthlogs.server.match.play.handler.CardHandler;
import com.hearthlogs.server.match.play.handler.GameHandler;
import com.hearthlogs.server.match.play.handler.PlayerHandler;
import com.hearthlogs.server.match.play.MatchResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class MatchPlayingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchPlayingService.class);

    private CardService cardService;
    private ActionHandler actionHandler;
    private CardHandler cardHandler;
    private GameHandler gameHandler;
    private PlayerHandler playerHandler;

    @Autowired
    public MatchPlayingService(CardService cardService,
                               ActionHandler actionHandler,
                               CardHandler cardHandler,
                               GameHandler gameHandler,
                               PlayerHandler playerHandler) {
        this.cardService = cardService;
        this.actionHandler = actionHandler;
        this.cardHandler = cardHandler;
        this.gameHandler = gameHandler;
        this.playerHandler = playerHandler;
    }

    public MatchResult processMatch(ParsedMatch parsedMatch, Integer rank) {

        MatchResult result = new MatchResult();
        result.setRank(rank);

        // Associate all the cards with their corresponding card details
        List<Card> cards = parsedMatch.getCards();
        for (Card c: cards) {
            c.setCardDetails(cardService.getCardDetails(c.getCardid()));
        }

        for (Activity activity: parsedMatch.getActivities()) {
            handle(result, parsedMatch, activity);
        }
        result.setFriendly(parsedMatch.getFriendlyPlayer());
        result.setOpposing(parsedMatch.getOpposingPlayer());


        return result;
    }


    public void handle(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        // Handler the domain first then any child actions. The reason for this is that the domain contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] BlockType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(result, parsedMatch, activity);
        doChildActivities(result, parsedMatch, activity.getChildren());
    }

    private void doChildActivities(MatchResult result, ParsedMatch parsedMatch, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                doActivity(result, parsedMatch, activity);
                doChildActivities(result, parsedMatch, activity.getChildren());
            }
        }
    }

    private void doActivity(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        playActivity(result, parsedMatch, activity);
        updateGameActivityData(parsedMatch, activity);
    }

    private void playActivity(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        actionHandler.handle(result, parsedMatch, activity);
        cardHandler.handle(result, parsedMatch, activity);
        gameHandler.handle(result, parsedMatch, activity);
        playerHandler.handle(result, parsedMatch, activity);
    }

    private void updateGameActivityData(ParsedMatch parsedMatch, Activity activity) {
        if (activity.isTagChange() || activity.isShowEntity() || activity.isHideEntity()) {
            copyNonNullProperties(activity.getEntity(), parsedMatch.getEntityById(activity.getEntityId()));
            if (activity.isShowEntity()) {
                Card c = ((Card) activity.getEntity());
                c.setCardDetails(cardService.getCardDetails(c.getCardid()));
            }
        }
    }

    public String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new LinkedHashSet<>();
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

}
