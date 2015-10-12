package com.hearthlogs.server.service.match.handler;

import com.hearthlogs.server.domain.Card;
import com.hearthlogs.server.domain.Game;
import com.hearthlogs.server.domain.Player;
import com.hearthlogs.server.match.Activity;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ActivityHandler {

    protected String TRUE = "1";
    protected String FALSE = "0";

    @Autowired
    protected CardService cardService;

    protected void handle(MatchContext context, Activity activity) {
        if (activity.isAction()) {
            if (activity.getEntity() instanceof Card) {
                handleAction(context, activity, getPlayer(context, (Card) activity.getEntity()), (Card) activity.getEntity(), (Card) activity.getTarget());
            }
        } else if (activity.isTagChange()) {
            if (activity.getEntity() instanceof Card) {
                Card before = (Card) context.getEntityById(activity.getEntityId());
                Card after = (Card) activity.getEntity();
                handleTagChange(context, activity, getPlayer(context, before), before, after);
            } else if (activity.getEntity() instanceof Player) {
                Player before = (Player) context.getEntityById(activity.getEntityId());
                Player after = (Player) activity.getEntity();
                handleTagChange(context, activity, before, after);
            } else if (activity.getEntity() instanceof Game) {
                Game before = (Game) context.getEntityById(activity.getEntityId());
                Game after = (Game) activity.getEntity();
                handleTagChange(context, activity, before, after);
            }
        }
    }

    protected void handleAction(MatchContext context, Activity activity, Player player, Card source, Card target) {

    }

    protected void handleTagChange(MatchContext context, Activity activity, Player player, Card before, Card after) {

    }

    protected void handleTagChange(MatchContext context, Activity activity, Player before, Player after) {

    }

    protected void handleTagChange(MatchContext context, Activity activity, Game before, Game after) {

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
