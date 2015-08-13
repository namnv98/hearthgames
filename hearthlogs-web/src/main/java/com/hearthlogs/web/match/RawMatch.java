package com.hearthlogs.web.match;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@Table(name = "matches")
public class RawMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "date_updated")
    private Timestamp dateUpdated;

    @Column(name = "raw_game")
    private byte[] rawGame;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "rank")
    private String rank;

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
}