package com.hearthlogs.server.service;

import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.*;
import com.hearthlogs.server.match.handler.CardHandler;
import com.hearthlogs.server.match.handler.GameHandler;
import com.hearthlogs.server.match.handler.PlayerHandler;
import com.hearthlogs.server.match.result.MatchResult;
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
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private CardService cardService;
    private CardHandler cardHandler;
    private GameHandler gameHandler;
    private PlayerHandler playerHandler;

    @Autowired
    public MatchService(CardService cardService,
                        CardHandler cardHandler,
                        GameHandler gameHandler,
                        PlayerHandler playerHandler) {
        this.cardService = cardService;
        this.cardHandler = cardHandler;
        this.gameHandler = gameHandler;
        this.playerHandler = playerHandler;
    }

    public MatchResult processMatch(MatchContext context, String rank) {

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


    public void handle(MatchResult result, MatchContext context, Activity activity) {
        // Handler the action first then any child actions. The reason for this is that the action contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] BlockType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(result, context, activity);
        doChildActivities(result, context, activity.getChildren());
    }

    private void doChildActivities(MatchResult result, MatchContext context, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                doChildActivities(result, context, activity.getChildren());
                doActivity(result, context, activity);
            }
        }
    }

    private void doActivity(MatchResult result, MatchContext context, Activity activity) {
        playActivity(result, context, activity);
        updateGameActivityData(context, activity);
    }

    private void playActivity(MatchResult result, MatchContext context, Activity activity) {
        cardHandler.handle(result, context, activity);
        gameHandler.handle(result, context, activity);
        playerHandler.handle(result, context, activity);
    }

    private void updateGameActivityData(MatchContext context, Activity activity) {
        if (activity.getType().equals(Activity.Type.TAG_CHANGE)) {
            copyNonNullProperties(activity.getEntity(), context.getEntityById(activity.getEntityId()));
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
