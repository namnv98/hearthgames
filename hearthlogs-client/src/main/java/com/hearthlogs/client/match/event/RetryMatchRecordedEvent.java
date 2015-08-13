package com.hearthlogs.client.match.event;

import com.hearthlogs.client.match.MatchData;
import org.springframework.context.ApplicationEvent;

import java.io.File;

public class RetryMatchRecordedEvent extends ApplicationEvent {

    private MatchData data;
    private File file;

    public RetryMatchRecordedEvent(Object source, MatchData data, File file) {
        super(source);
        this.data = data;
        this.file = file;
    }

    public MatchData getData() {
        return data;
    }

    public File getFile() {
        return file;
    }
}
