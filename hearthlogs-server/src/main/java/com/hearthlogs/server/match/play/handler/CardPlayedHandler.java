package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.parse.domain.Zone;
import com.hearthlogs.server.match.play.MatchResult;

public class CardPlayedHandler implements Handler {

    @Override
    public boolean supports(MatchResult result, ParseContext context, Activity activity) {
        return (activity.isTagChange() || activity.isShowEntity() || activity.isHideEntity()) && (activity.getDelta() instanceof Card) && context.getAfter(activity).getZone() != null;
    }

    @Override
    public boolean handle(MatchResult result, ParseContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);
        Player player = context.getPlayerForCard(before);

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype()) ) {
            result.addCardPlayed(player, before);
            System.out.println(player.getName() + " has played a card from HAND : " + before.getName());
        } else if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
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
        }

        // Has a card come into play/secret
        if ((Zone.PLAY.eq(after.getZone()) && !Zone.PLAY.eq(before.getZone()) && Card.Type.MINION.eq(before.getCardtype())) ||
            (Zone.SECRET.eq(after.getZone()) && !Zone.SECRET.eq(before.getZone()) && Card.Type.SPELL.eq(before.getCardtype()))) {
            if (context.isFriendly(player)) {
                result.getCurrentTurn().getFriendlyCardsPutInPlay().add(before);
            } else {
                result.getCurrentTurn().getOpposingCardsPutInPlay().add(before);
            }
            return true;
        // Has a card been removed from play
        } else if (Zone.PLAY.eq(before.getZone()) && !Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            if (context.isFriendly(player)) {
                result.getCurrentTurn().getFriendlyCardsRemovedFromPlay().add(before);
            } else {
                result.getCurrentTurn().getOpposingCardsRemovedFromPlay().add(before);
            }
            return true;
        }
        return false;
    }
}