package com.hearthlogs.server.match.handler;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.Game;
import com.hearthlogs.server.match.domain.Player;
import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import org.springframework.stereotype.Component;

@Component
public abstract class ActivityHandler {

    protected String TRUE_OR_ONE = "1";
    protected String FALSE_OR_ZERO = "0";

    public void handle(MatchResult result, MatchContext context, Activity activity) {
        if (activity.isTagChange()) {
            if (activity.getEntity() instanceof Card) {
                Card before = (Card) context.getEntityById(activity.getEntityId());
                Card after = (Card) activity.getEntity();
                handleTagChange(result, context, activity, getPlayer(context, before), before, after);
            } else if (activity.getEntity() instanceof Player) {
                Player before = (Player) context.getEntityById(activity.getEntityId());
                Player after = (Player) activity.getEntity();
                handleTagChange(result, context, activity, before, after);
            } else if (activity.getEntity() instanceof Game) {
                Game before = context.getGame();
                Game after = (Game) activity.getEntity();
                handleTagChange(result, context, activity, before, after);
            }
        } else if (activity.isNewCard()) {
            handleNewCard(result, context, activity);
        } else if (activity.isNewGame()) {
            handleNewGame(result, context, activity);
        }
    }

    protected void handleNewGame(MatchResult result, MatchContext context, Activity activity) {

    }

    protected void handleNewCard(MatchResult result, MatchContext context, Activity activity) {

    }

    protected void handleTagChange(MatchResult result, MatchContext context, Activity activity, Player player, Card before, Card after) {

    }

    protected void handleTagChange(MatchResult result, MatchContext context, Activity activity, Player before, Player after) {

    }

    protected void handleTagChange(MatchResult result, MatchContext context, Activity activity, Game before, Game after) {

    }

    protected Player getPlayer(MatchContext context, Card card) {
        if (context.getFriendlyPlayer().getController().equals(card.getController())) return context.getFriendlyPlayer();
        return context.getOpposingPlayer();
    }

    protected String getName(Card card) {
        if ("".equals(card.getCardid())) return "a card";
        String name = card.getCardDetails().getName();
        return "".equals(name) ? "a card" : name;
    }
}
