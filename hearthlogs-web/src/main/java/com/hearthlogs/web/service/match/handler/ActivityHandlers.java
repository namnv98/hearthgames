package com.hearthlogs.web.service.match.handler;

import com.hearthlogs.web.match.Activity;
import com.hearthlogs.web.match.MatchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityHandlers {

    private AttackHandler attackHandler;
    private PowerHandler powerHandler;
    private CardHandler cardHandler;
    private GameHandler gameHandler;
    private PlayerHandler playerHandler;

    @Autowired
    public ActivityHandlers(AttackHandler attackHandler,
                            PowerHandler powerHandler,
                            CardHandler cardHandler,
                            GameHandler gameHandler,
                            PlayerHandler playerHandler) {
        this.attackHandler = attackHandler;
        this.powerHandler = powerHandler;
        this.cardHandler = cardHandler;
        this.gameHandler = gameHandler;
        this.playerHandler = playerHandler;
    }


    public void processActivity(MatchContext context, Activity activity) {
        attackHandler.handle(context, activity);
        powerHandler.handle(context, activity);
        cardHandler.handle(context, activity);
        gameHandler.handle(context, activity);
        playerHandler.handle(context, activity);
    }
}
