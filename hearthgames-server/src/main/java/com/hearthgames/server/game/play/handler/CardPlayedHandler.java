package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameResult;

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

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && Card.isSpell(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.SECRET, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            result.addCardPlayed(Zone.DECK, Zone.PLAY, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.SECRET.eq(after.getZone()) && Card.isSpell(before, after)) {
            result.addCardPlayed(Zone.DECK, Zone.SECRET, player, before);
        } else if (Zone.GRAVEYARD.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            result.addCardPlayed(Zone.GRAVEYARD, Zone.PLAY, player, before);
        } else if (Zone.DELAYED_PLAY.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            result.addCardPlayed(Zone.PLAY, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isSpell(before, after)) {
            result.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone())) {
            Card cause = (Card) activity.getParent().getDelta();
            Player causeController = context.getPlayer(cause);
            Player cardController = context.getPlayer(before);
            result.addCardDiscarded(causeController, cardController, Zone.HAND, Zone.GRAVEYARD, player, before, cause);
        }

        return false;
    }
}