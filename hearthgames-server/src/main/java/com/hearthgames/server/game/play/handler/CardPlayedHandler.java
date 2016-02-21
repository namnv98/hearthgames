package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameContext;

public class CardPlayedHandler implements Handler {

    @Override
    public boolean supports(GameContext gameContext) {
        return isTagChangeOrShowEntityOrHideEntity(gameContext) &&
               gameContext.getActivity().isCard() &&
               gameContext.getAfter().getZone() != null;
    }

    private boolean isTagChangeOrShowEntityOrHideEntity(GameContext gameContext) {
        return gameContext.getActivity().isTagChange() || gameContext.getActivity().isShowEntity() || gameContext.getActivity().isHideEntity();
    }

    @Override
    public boolean handle(GameContext gameContext) {
        Card before = gameContext.getBefore();
        Card after = gameContext.getAfter();
        Player player = gameContext.getGameState().getPlayerForCard(before);

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            gameContext.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.SECRET.eq(after.getZone())) {
            gameContext.addCardPlayed(Zone.HAND, Zone.SECRET, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            gameContext.addCardPlayed(Zone.DECK, Zone.PLAY, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.SECRET.eq(after.getZone())) {
            gameContext.addCardPlayed(Zone.DECK, Zone.SECRET, player, before);
        } else if (Zone.GRAVEYARD.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            gameContext.addCardPlayed(Zone.GRAVEYARD, Zone.PLAY, player, before);
        } else if (Zone.DELAYED_PLAY.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            gameContext.addCardPlayed(Zone.PLAY, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isSpell(before, after)) {
            gameContext.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone())) {
            Card cause = gameContext.getActivity().getParent().getDelta();
            Player causeController = gameContext.getGameState().getPlayer(cause);
            Player cardController = gameContext.getGameState().getPlayer(before);
            gameContext.addCardDiscarded(causeController, cardController, Zone.HAND, Zone.GRAVEYARD, player, before, cause);
        }

        return false;
    }
}