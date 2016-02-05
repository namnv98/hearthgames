package com.hearthgames.server.database.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlayerWinLossSummary {

    @Id
    private String gameAccountId;
    private String playerName;
    private int wins;
    private int losses;

    public String getGameAccountId() {
        return gameAccountId;
    }

    public void setGameAccountId(String gameAccountId) {
        this.gameAccountId = gameAccountId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
}
