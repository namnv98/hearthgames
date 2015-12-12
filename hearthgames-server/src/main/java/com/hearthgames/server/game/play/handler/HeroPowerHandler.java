package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.Activity;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.play.GameResult;

public class HeroPowerHandler implements Handler {

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isPower() && activity.getDelta() instanceof Card;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card card = context.getBefore(activity);
        Player player = context.getPlayerForCard(card);

        if (Card.Type.HERO_POWER.eq(card.getCardtype())) {
            result.addHeroPowerUsed(player, card);
            return true;
        }
        return false;
    }
}
