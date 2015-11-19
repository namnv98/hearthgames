package com.hearthlogs.server.game.play.handler;

import java.util.ArrayList;
import java.util.List;

public class PlayHandlers {

    private List<Handler> handlers = new ArrayList<>();

    public PlayHandlers() {
        handlers.add(new ArmorChangeHandler());
        handlers.add(new AttachDetachHandler());
        handlers.add(new AttackChangeHandler());
        handlers.add(new CardCreationHandler());
        handlers.add(new CardDrawnHandler());
        handlers.add(new CardPlayedHandler());
        handlers.add(new DamageHandler());
        handlers.add(new FrozenHandler());
        handlers.add(new GameHandler());
        handlers.add(new HealthChangeHandler());
        handlers.add(new HeroHealthChangeHandler());
        handlers.add(new HeroPowerHandler());
        handlers.add(new JoustHandler());
        handlers.add(new KillHandler());
        handlers.add(new MulliganCardHandler());
        handlers.add(new NewGameHandler());
        handlers.add(new PlayerHandler());
        handlers.add(new TriggerHandler());
        handlers.add(new ZonePositionChangeHandler());
    }

    public List<Handler> getHandlers() {
        return handlers;
    }
}
