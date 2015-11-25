package com.hearthlogs.client.match.event;

import com.hearthlogs.client.match.GameData;
import org.springframework.context.ApplicationEvent;

public class GameRecordedEvent extends ApplicationEvent {

    private GameData data;

    public GameRecordedEvent(Object source, GameData data) {
        super(source);
        this.data = data;
    }

    public GameData getData() {
        return data;
    }
}
