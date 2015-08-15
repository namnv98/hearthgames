package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Entity;
import com.hearthlogs.web.domain.Game;
import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ActivityHandler {

    @Autowired
    protected CardService cardService;

    protected void handle(MatchContext context, Activity activity) {
        CompletedMatch match = context.getCompletedMatch();
        if (activity.isAction()) {
            if (activity.getEntity() instanceof Card) {
                handleAction(context, match, activity, getPlayer(context, (Card) activity.getEntity()), (Card) activity.getEntity(), (Card) activity.getTarget());
            }
        } else if (activity.isTagChange()) {
            if (activity.getEntity() instanceof Card) {
                Card before = (Card) context.getEntityById(activity.getEntityId());
                Card after = (Card) activity.getEntity();
                handleTagChange(context, match, activity, getPlayer(context, before), before, after);
            } else if (activity.getEntity() instanceof Player) {
                Player before = (Player) context.getEntityById(activity.getEntityId());
                Player after = (Player) activity.getEntity();
                handleTagChange(context, match, activity, before, after);
            } else if (activity.getEntity() instanceof Game) {
                Game before = (Game) context.getEntityById(activity.getEntityId());
                Game after = (Game) activity.getEntity();
                handleTagChange(context, match, activity, before, after);
            }
        }
    }

    protected void handleAction(MatchContext context, CompletedMatch match, Activity activity, Player player, Card source, Card target) {

    }

    protected void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Player player, Card before, Card after) {

    }

    protected void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Player before, Player after) {

    }

    protected void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Game before, Game after) {

    }

    protected Player getPlayer(MatchContext context, Card card) {
        if (context.getFriendlyPlayer().getController().equals(card.getController())) return context.getFriendlyPlayer();
        return context.getOpposingPlayer();
    }

    protected String getName(String id) {
        if ("".equals(id)) return "a card";
        String name = cardService.getName(id);
        return "".equals(name) ? "a card" : name;
    }

    protected void printHeroHealth(Player player, Card heroCard) {
        int health = Integer.valueOf(heroCard.getHealth());
        int damage = heroCard.getDamage() != null ? Integer.valueOf(heroCard.getDamage()) : 0;

        System.out.println(player.getName() + " Hero Health = " + (health - damage));
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }
}
