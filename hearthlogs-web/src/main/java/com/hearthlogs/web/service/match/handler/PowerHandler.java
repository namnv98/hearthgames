package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.domain.Card;
import com.hearthlogs.web.domain.Player;
import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class PowerHandler extends ActivityHandler {

    public void handleAction(MatchContext context, Activity activity, Player player, Card source, Card target) {
        if (activity.isPower()) {
            if (target == null) {
                System.out.println(getName(source.getCardid()) + " has been cast");
            } else {
                System.out.println(getName(source.getCardid()) + " has been cast on " + getName(target.getCardid()));
            }
        }
    }

}
