package com.hearthgames.server.game.log.domain;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class LogLineData {

    private LocalDateTime dateTime;
    private String line;

    public LogLineData(String timestamp, String line) {
        this.dateTime = !StringUtils.isEmpty(timestamp) ? LocalDateTime.parse(timestamp.replace(" ","T")) : null;
        this.line = line.replace("[Power] GameState.DebugPrintPower() - ", "");
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
