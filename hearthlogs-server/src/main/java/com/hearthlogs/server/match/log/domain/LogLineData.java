package com.hearthlogs.server.match.log.domain;

import java.time.LocalDateTime;

public class LogLineData {

    private LocalDateTime dateTime;
    private String line;

    public LogLineData(String timestamp, String line) {
        this.dateTime = LocalDateTime.parse(timestamp.replace(" ","T"));
        this.line = line;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTrimmedLine() {
        return line != null ? line.trim() : null;
    }

}
