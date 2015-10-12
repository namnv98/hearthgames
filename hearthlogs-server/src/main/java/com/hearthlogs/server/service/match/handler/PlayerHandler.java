package com.hearthlogs.server.service.match.handler;

import com.hearthlogs.server.domain.Player;
import com.hearthlogs.server.match.Activity;
import com.hearthlogs.server.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class PlayerHandler extends ActivityHandler {

    protected void handleTagChange(MatchContext context, Activity activity, Player before, Player after) {
        if (after.getPlaystate() != null && Player.PlayState.QUIT.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has quit.");
            context.setQuitter(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.WON.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has won.");
            context.setWinner(before);
        }
        if (after.getPlaystate() != null && Player.PlayState.LOST.eq(after.getPlaystate())) {
            System.out.println(before.getName() + " has lost.");
            context.setLoser(before);
        }
        if (TRUE.equals(before.getFirstPlayer())) {
            context.setFirst(before);
        }
    }
}