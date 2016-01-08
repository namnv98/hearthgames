package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.PlayContext;

public class ZonePositionChangeHandler implements Handler {
    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && playContext.getActivity().getDelta() instanceof Card && playContext.getContext().getAfter(playContext.getActivity()).getZonePosition() != null && playContext.getContext().getGameEntity().isMulliganOver();
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

//        result.addZonePositionChange(before, Zone.valueOf(before.getZone()), Integer.parseInt(after.getZonePosition()));
//        result.addActionLog(before.getCardDetails() == null ? "unknown card" : before.getName() + " has moved to " + before.getZone() + ", position " + after.getZonePosition());
        return true;
    }
}
