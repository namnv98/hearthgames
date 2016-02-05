package com.hearthgames.server.service;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;
import com.hearthgames.server.game.play.handler.PlayHandlers;
import com.hearthgames.server.game.play.handler.Handler;
import com.hearthgames.server.game.play.GameResult;
import org.apache.commons.collections.CollectionUtils;
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
public class GamePlayingService {

    private CardService cardService;
    private PlayHandlers playHandlers = new PlayHandlers();

    @Autowired
    public GamePlayingService(CardService cardService) {
        this.cardService = cardService;
    }

    public GameResult processGame(GameContext context, Integer rank) {

        GameResult result = new GameResult();
        result.setRank(rank);

        // Associate all the cards with their corresponding card details
        List<Card> cards = context.getCards();
        for (Card c: cards) {
            c.setCardDetails(cardService.getCardDetails(c.getCardid()));
        }

        PlayContext playContext = new PlayContext(context, result);
        for (Activity activity: context.getActivities()) {
            playContext.setActivity(activity);
            handle(playContext);
        }
        playContext.addLastBoard();

        return result;
    }


    public void handle(PlayContext playContext) {
        // Handler the domain first then any child actions. The reason for this is that the domain contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] BlockType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(playContext);
        doChildActivities(playContext, playContext.getActivity().getChildren());
        playContext.addBoard();
    }

    private void doChildActivities(PlayContext playContext, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                playContext.setActivity(activity);
                doActivity(playContext);
                doChildActivities(playContext, activity.getChildren());
            }
        }
    }

    private void doActivity(PlayContext playContext) {
        playActivity(playContext);
        updateGameActivityData(playContext);
    }

    private void playActivity(PlayContext playContext) {
        for (Handler handler: playHandlers.getHandlers()) {
            if (handler.supports(playContext)) {
                boolean handled = handler.handle(playContext);
                if (handled) {
                    return;
                }
            }
        }
    }

    private void updateGameActivityData(PlayContext playContext) {
        if (playContext.getActivity().isTagChange() || playContext.getActivity().isShowEntity() || playContext.getActivity().isHideEntity()) {
            copyNonNullProperties(playContext.getActivity().getDelta(), playContext.getContext().getEntityById(playContext.getActivity().getEntityId()));
            if (playContext.getActivity().isShowEntity()) {
                Card c = (Card) playContext.getActivity().getDelta();
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
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

}
