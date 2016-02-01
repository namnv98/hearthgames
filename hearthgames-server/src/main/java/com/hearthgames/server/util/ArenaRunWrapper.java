package com.hearthgames.server.util;

import com.hearthgames.server.database.domain.ArenaRun;

import java.util.ArrayList;
import java.util.List;

public class ArenaRunWrapper {

    private int pages;
    private List<ArenaRun> runs = new ArrayList<>();

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ArenaRun> getRuns() {
        return runs;
    }

    public void setRuns(List<ArenaRun> runs) {
        this.runs = runs;
    }
}
