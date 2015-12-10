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

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && isMinion(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
            result.addActionLog(player.getName() + " has played a minion from HAND : " + before.getName());
        } else if (Zone.HAND.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && isSpell(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.SECRET, player, before);
            result.addActionLog(player.getName() + " has played a secret from HAND : " + before.getName());
        } else if (Zone.DECK.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && isMinion(before, after)) {
            result.addCardPlayed(Zone.DECK, Zone.PLAY, player, before);
            result.addActionLog(player.getName() + " has played a minion from DECK : " + before.getName());
        } else if (Zone.DECK.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && isSpell(before, after)) {
            result.addCardPlayed(Zone.DECK, Zone.SECRET, player, before);
            result.addActionLog(player.getName() + " has played a secret from DECK : " + before.getName());
        } else if (Zone.GRAVEYARD.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && isMinion(before, after)) {
            result.addCardPlayed(Zone.GRAVEYARD, Zone.PLAY, player, before);
            result.addActionLog(player.getName() + " has played a minion from GRAVEYARD : " + before.getName());
        } else if (Zone.DELAYED_PLAY.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && isMinion(before, after)) {
            result.addCardPlayed(Zone.PLAY, Zone.PLAY, player, before);
            result.addActionLog(player.getName() + " has gained a minion : " + before.getName());
        } else if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && isSpell(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
            result.addActionLog(player.getName() + " has cast a spell : " + before.getName());
        } else if (Zone.HAND.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone())) {
            Card cause = (Card) activity.getParent().getDelta();
            String causeSide = context.getSide(cause);
            String cardSide = context.getSide(before);
            result.addCardDiscarded(causeSide, cardSide, Zone.HAND, Zone.GRAVEYARD, player, before, cause);
            result.addActionLog(cause.getName() + " has caused " + player.getName() + " to discard " + before.getName());
        }

        return false;
    }

    private boolean isMinion(Card before, Card after) {
        return Card.Type.MINION.eq(before.getCardtype()) || Card.Type.MINION.eq(after.getCardtype());
    }

    private boolean isSpell(Card before, Card after) {
        return Card.Type.SPELL.eq(before.getCardtype()) || Card.Type.MINION.eq(after.getCardtype());
    }
}