package com.hearthlogs.server.game.play.handler;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Activity;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.play.GameResult;

public class CardPlayedHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return (activity.isTagChange() || activity.isShowEntity() || activity.isHideEntity()) && (activity.getDelta() instanceof Card) && context.getAfter(activity).getZone() != null;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);
        Player player = context.getPlayerForCard(before);

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a minion from HAND : " + before.getName());
        } else if (Zone.HAND.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && Card.Type.SPELL.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a secret from HAND : " + before.getName());
        } else if (Zone.DECK.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a minion from DECK : " + before.getName());
        } else if (Zone.DECK.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && Card.Type.SPELL.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a secret from DECK : " + before.getName());
        } else if (Zone.GRAVEYARD.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a minion from GRAVEYARD : " + before.getName());
        } else if (Zone.DELAYED_PLAY.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has gained a minion : " + before.getName());
        }

        return false;
    }
}