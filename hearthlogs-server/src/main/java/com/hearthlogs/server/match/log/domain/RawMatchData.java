package com.hearthlogs.server.match.log.domain;

import java.util.List;

public class RawMatchData {

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
}
