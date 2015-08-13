package com.hearthlogs.client.match.event;

import com.hearthlogs.client.match.MatchData;
import org.springframework.context.ApplicationEvent;

public class SaveMatchLocallyEvent extends ApplicationEvent {

    private MatchData data;

    public SaveMatchLocallyEvent(Object source, MatchData data) {
        super(source);
        this.data = data;
    }

    public MatchData getData() {
        return data;
    }
}
