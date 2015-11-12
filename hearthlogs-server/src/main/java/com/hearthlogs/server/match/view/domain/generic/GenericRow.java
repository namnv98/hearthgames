package com.hearthlogs.server.match.view.domain.generic;

import java.util.ArrayList;
import java.util.List;

public class GenericRow {

    private List<GenericColumn> columns = new ArrayList<>();

    public List<GenericColumn> getColumns() {
        return columns;
    }

    public void addColumn(GenericColumn column) {
        columns.add(column);
    }
}