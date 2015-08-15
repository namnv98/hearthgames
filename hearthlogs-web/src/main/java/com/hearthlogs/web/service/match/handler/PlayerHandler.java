package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.CompletedMatch;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class PlayerHandler extends ActivityHandler {

    protected void handleTagChange(MatchContext context, CompletedMatch match, Activity activity, Player before, Player after) {
        if (after.getPlaystate() != null && "QUIT".equals(after.getPlaystate())) {
            System.out.println(before.getName() + " has quit.");
            match.setQuitter(before.getName());
        }
        if (after.getPlaystate() != null && "WON".equals(after.getPlaystate())) {
            System.out.println(before.getName() + " has won.");
            match.setWinner(before.getName());
        }
        if (after.getPlaystate() != null && "LOST".equals(after.getPlaystate())) {
            System.out.println(before.getName() + " has lost.");
            match.setLoser(before.getName());
        }
    }

}
