package com.hearthlogs.server.service;

import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.handler.Handler;
import com.hearthlogs.server.match.play.handler.PlayHandlers;
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
    private PlayHandlers playHandlers = new PlayHandlers();

    @Autowired
    public MatchPlayingService(CardService cardService) {
        this.cardService = cardService;
    }

    public MatchResult processMatch(ParseContext context, Integer rank) {

        MatchResult result = new MatchResult();
        result.setRank(rank);

        // Associate all the cards with their corresponding card details
        List<Card> cards = context.getCards();
        for (Card c: cards) {
            c.setCardDetails(cardService.getCardDetails(c.getCardid()));
        }

        for (Activity activity: context.getActivities()) {
            handle(result, context, activity);
        }
        result.setFriendly(context.getFriendlyPlayer());
        result.setOpposing(context.getOpposingPlayer());


        return result;
    }


    public void handle(MatchResult result, ParseContext context, Activity activity) {
        // Handler the domain first then any child actions. The reason for this is that the domain contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] BlockType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(result, context, activity);
        doChildActivities(result, context, activity.getChildren());
    }

    private void doChildActivities(MatchResult result, ParseContext context, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                doActivity(result, context, activity);
                doChildActivities(result, context, activity.getChildren());
            }
        }
    }

    private void doActivity(MatchResult result, ParseContext context, Activity activity) {
        playActivity(result, context, activity);
        updateGameActivityData(context, activity);
    }

    private void playActivity(MatchResult result, ParseContext context, Activity activity) {
        for (Handler handler: playHandlers.getHandlers()) {
            if (handler.supports(result, context, activity)) {
                boolean handled = handler.handle(result, context, activity);
                if (handled) {
                    return;
                }
            }
        }
    }

    private void updateGameActivityData(ParseContext context, Activity activity) {
        if (activity.isTagChange() || activity.isShowEntity() || activity.isHideEntity()) {
            copyNonNullProperties(activity.getDelta(), context.getEntityById(activity.getEntityId()));
            if (activity.isShowEntity()) {
                Card c = ((Card) activity.getDelta());
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
