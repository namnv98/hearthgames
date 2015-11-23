package com.hearthlogs.server.game.analysis.domain;

import java.util.ArrayList;
import java.util.List;

public class TurnRow {

    private int type;

    public TurnRow(int type) {
        this.type = type;
    }

    private List<TurnColumn> columns = new ArrayList<>();

    public void addColumn(TurnColumn column) {
        columns.add(column);
    }

    public List<TurnColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TurnColumn> columns) {
        this.columns = columns;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
