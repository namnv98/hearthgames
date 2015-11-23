package com.hearthlogs.server.game.analysis.domain;

import java.util.ArrayList;
import java.util.List;

public class TurnInfo {

    private String turnNumber;
    private String whoseTurn;
    private String turnClass;

    private List<TurnRow> rows = new ArrayList<>();

    public List<TurnRow> getRows() {
        return rows;
    }

    public void setRows(List<TurnRow> rows) {
        this.rows = rows;
    }

    public String getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(String turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(String whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public String getTurnClass() {
        return turnClass;
    }

    public void setTurnClass(String turnClass) {
        this.turnClass = turnClass;
    }
}
