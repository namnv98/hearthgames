package com.hearthgames.server.game.log.domain;

import java.util.List;

public class RawGameData {

    private List<String> rawLines;
    private List<LogLineData> lines;
    private Integer rank;
    private GameType gameType;


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

    public GameType getGameType() {
        return gameType == null ? GameType.UNKNOWN : gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}
