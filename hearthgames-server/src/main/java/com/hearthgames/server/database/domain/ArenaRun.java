package com.hearthgames.server.database.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.database.domain.json.ArenaRunSerializer;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@JsonSerialize(using = ArenaRunSerializer.class)
public class ArenaRun {

    @Id
    private String arenaDeckId;
    private String gameAccountId;
    private String playerName;
    private String playerClass;
    private int wins;
    private int losses;

    public String getArenaDeckId() {
        return arenaDeckId;
    }

    public void setArenaDeckId(String arenaDeckId) {
        this.arenaDeckId = arenaDeckId;
    }

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

    public String getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
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
