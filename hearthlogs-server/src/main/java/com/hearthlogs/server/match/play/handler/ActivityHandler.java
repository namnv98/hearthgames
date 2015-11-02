package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.play.domain.ActionFactory;
import com.hearthlogs.server.match.play.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ActivityHandler {

    protected String TRUE_OR_ONE = "1";
    protected String FALSE_OR_ZERO = "0";

    protected ActionFactory actionFactory;

    @Autowired
    public ActivityHandler(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    public void handle(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        if (activity.isAction()) {
            if (activity.getEntity() instanceof Card) {
                Player player = ((Card) activity.getEntity()).getController().equals(parsedMatch.getFriendlyPlayer().getController()) ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer();
                handleAction(result, parsedMatch, activity, player, (Card) activity.getEntity(), (Card) activity.getTarget());
            }
        } else if (activity.isTagChange()) {
            if (activity.getEntity() instanceof Card) {
                Card before = (Card) parsedMatch.getEntityById(activity.getEntityId());
                Card after = (Card) activity.getEntity();
                Player player = before.getController().equals(parsedMatch.getFriendlyPlayer().getController()) ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer();
                handleCardTagChange(result, parsedMatch, activity, player, before, after);
            } else if (activity.getEntity() instanceof Player) {
                Player before = (Player) parsedMatch.getEntityById(activity.getEntityId());
                Player after = (Player) activity.getEntity();
                handlePlayerTagChange(result, parsedMatch, activity, before, after);
            } else if (activity.getEntity() instanceof Game) {
                Game before = parsedMatch.getGame();
                Game after = (Game) activity.getEntity();
                handleGameTagChange(result, parsedMatch, activity, before, after);
            }
        } else if (activity.isShowEntity()) {
            Card before = (Card) parsedMatch.getEntityById(activity.getEntityId());
            Card after = (Card) activity.getEntity();
            Player player = before.getController().equals(parsedMatch.getFriendlyPlayer().getController()) ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer();
            handleShowEntity(result, parsedMatch, activity, player, before, after);
        } else if (activity.isNewCard()) {
            handleNewCard(result, parsedMatch, activity);
        } else if (activity.isNewGame()) {
            handleNewGame(result, parsedMatch, activity);
        }
    }

    protected void handleAction(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card source, Card target) {

    }

    protected void handleNewGame(MatchResult result, ParsedMatch parsedMatch, Activity activity) {

    }

    protected void handleNewCard(MatchResult result, ParsedMatch parsedMatch, Activity activity) {

    }

    protected void handleCardTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after) {

    }

    protected void handlePlayerTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player before, Player after) {

    }

    protected void handleGameTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Game before, Game after) {

    }

    protected void handleShowEntity(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after) {

    }
}
