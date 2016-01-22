package com.hearthgames.server.game.log.domain;

import java.util.List;

public class RawGameData {

    private List<String> rawLines;
    private List<LogLineData> lines;
    private Integer rank;
    private GameType gameType;

    private String arenaDeckId;
    private List<String> arenaDeckCards;

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

    public String getArenaDeckId() {
        return arenaDeckId;
    }

    public void setArenaDeckId(String arenaDeckId) {
        this.arenaDeckId = arenaDeckId;
    }

    public List<String> getArenaDeckCards() {
        return arenaDeckCards;
    }

    public void setArenaDeckCards(List<String> arenaDeckCards) {
        this.arenaDeckCards = arenaDeckCards;
    }
}
