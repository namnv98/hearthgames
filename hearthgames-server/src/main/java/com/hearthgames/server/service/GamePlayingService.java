package com.hearthgames.server.service;

import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.GameContext;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.handler.Handler;
import com.hearthgames.server.game.play.handler.PlayHandlers;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Collection;
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

    public GameResult processGame(GameState gameState, Integer rank) {

        GameResult result = new GameResult();
        result.setRank(rank);

        // Associate all the cards with their corresponding card details
        Collection<Card> cards = gameState.getCards().values();
        for (Card c: cards) {
            c.setCardDetails(cardService.getCardDetails(c.getCardid()));
        }

        GameContext gameContext = new GameContext(gameState, result);
        for (Activity activity: gameState.getActivities()) {
            gameContext.setActivity(activity);
            handle(gameContext);
        }
        gameContext.addLastBoard();

        return result;
    }


    public void handle(GameContext gameContext) {
        // Handler the domain first then any child actions. The reason for this is that the domain contains a high level abstraction what is happening
        // For example:
        //    ACTION_START Entity=[name=Haunted Creeper id=75 zone=PLAY zonePos=1 cardId=FP1_002 player=2] BlockType=ATTACK Index=-1 Target=[name=Jaina Proudmoore id=4 zone=PLAY zonePos=0 cardId=HERO_08 player=1]
        // means the Haunted Creeper minion is attacking Jaina Proudmoore
        // The details of the attack are in the child actions
        doActivity(gameContext);
        doChildActivities(gameContext, gameContext.getActivity().getChildren());
        gameContext.addBoard();
    }

    private void doChildActivities(GameContext gameContext, List<Activity> activities) {
        if (!CollectionUtils.isEmpty(activities)) {
            for (Activity activity : activities) {
                gameContext.setActivity(activity);
                doActivity(gameContext);
                doChildActivities(gameContext, activity.getChildren());
            }
        }
    }

    private void doActivity(GameContext gameContext) {
        playActivity(gameContext);
        updateGameActivityData(gameContext);
    }

    private void playActivity(GameContext gameContext) {
        for (Handler handler: playHandlers.getHandlers()) {
            if (handler.supports(gameContext)) {
                boolean handled = handler.handle(gameContext);
                if (handled) {
                    return;
                }
            }
        }
    }

    private void updateGameActivityData(GameContext gameContext) {
        if (gameContext.getActivity().isTagChange() || gameContext.getActivity().isShowEntity() || gameContext.getActivity().isHideEntity()) {
            copyNonNullProperties(gameContext.getActivity().getDelta(), gameContext.getGameState().getEntityById(gameContext.getActivity().getEntityId()));
            if (gameContext.getActivity().isShowEntity()) {
                Card c = gameContext.getActivity().getDelta();
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
