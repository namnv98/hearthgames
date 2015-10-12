package com.hearthlogs.server.service.match.handler;

import com.hearthlogs.server.domain.Card;
import com.hearthlogs.server.domain.Player;
import com.hearthlogs.server.match.Activity;
import com.hearthlogs.server.match.MatchContext;
import org.springframework.stereotype.Component;

@Component
public class AttackHandler extends ActivityHandler {

    public void handleAction(MatchContext context, Activity activity, Player player, Card source, Card target) {
        if (activity.isAttack()) {
            System.out.println(getName(source.getCardid()) + " has attacked " + getName(target.getCardid()));
        }
    }

}
