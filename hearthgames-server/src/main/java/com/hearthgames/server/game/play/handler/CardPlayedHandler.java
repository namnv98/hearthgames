package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.PlayContext;

public class CardPlayedHandler implements Handler {

    @Override
    public boolean supports(PlayContext playContext) {
        return (playContext.getActivity().isTagChange() || playContext.getActivity().isShowEntity() || playContext.getActivity().isHideEntity()) && (playContext.getActivity().getDelta() instanceof Card) && playContext.getContext().getAfter(playContext.getActivity()).getZone() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());
        Player player = playContext.getContext().getPlayerForCard(before);

        if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            playContext.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.SECRET.eq(after.getZone())) {
            playContext.addCardPlayed(Zone.HAND, Zone.SECRET, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            playContext.addCardPlayed(Zone.DECK, Zone.PLAY, player, before);
        } else if (Zone.DECK.eq(before.getZone()) && Zone.SECRET.eq(after.getZone())) {
            playContext.addCardPlayed(Zone.DECK, Zone.SECRET, player, before);
        } else if (Zone.GRAVEYARD.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            playContext.addCardPlayed(Zone.GRAVEYARD, Zone.PLAY, player, before);
        } else if (Zone.DELAYED_PLAY.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isMinion(before, after)) {
            playContext.addCardPlayed(Zone.PLAY, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.isSpell(before, after)) {
            playContext.addCardPlayed(Zone.HAND, Zone.PLAY, player, before);
        } else if (Zone.HAND.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone())) {
            Card cause = (Card) playContext.getActivity().getParent().getDelta();
            Player causeController = playContext.getContext().getPlayer(cause);
            Player cardController = playContext.getContext().getPlayer(before);
            playContext.addCardDiscarded(causeController, cardController, Zone.HAND, Zone.GRAVEYARD, player, before, cause);
        }

        return false;
    }
}