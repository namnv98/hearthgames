package com.hearthlogs.server.game.log.domain;

import java.util.List;

public class RawMatchData {

    private List<String> rawLines;
    private List<LogLineData> lines;
    private Integer rank;

    public List<LogLineData> getLines() {
        return lines;
    }

    public void setLines(List<LogLineData> lines) {
        this.lines = lines;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public List<String> getRawLines() {
        return rawLines;
    }

    public void setRawLines(List<String> rawLines) {
        this.rawLines = rawLines;
    }
}
