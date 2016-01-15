package com.hearthgames.server.util;

import com.hearthgames.server.database.domain.GamePlayed;

import java.util.ArrayList;
import java.util.List;

public class GamesPlayedWrapper {
    private boolean ranked;
    private int pages;
    private List<GamePlayed> games = new ArrayList<>();

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<GamePlayed> getGames() {
        return games;
    }

    public void setGames(List<GamePlayed> games) {
        this.games = games;
    }
}