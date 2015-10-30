package com.hearthlogs.server.match;

import com.hearthlogs.server.match.result.Turn;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

public class Match {

    private Long id;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private byte[] rawGame;
    private Timestamp startTime;
    private Timestamp endTime;
    private String rank;
    private String friendlyAccountId;
    private String opposingAccountId;
    private String friendlyPlayer; // the name of the friendly player
    private String opposingPlayer;
    private String first; // the player who went first
    private String winner; // will contain the name of the player who won
    private String loser;
    private String quitter; // the name of the player who quit if any

    private Set<Turn> turns = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public byte[] getRawGame() {
        return rawGame;
    }

    public void setRawGame(byte[] rawGame) {
        this.rawGame = rawGame;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Set<Turn> getTurns() {
        return turns;
    }

    public void setTurns(Set<Turn> turns) {
        this.turns = turns;
    }
}
