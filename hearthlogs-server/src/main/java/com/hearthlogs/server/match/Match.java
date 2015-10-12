package com.hearthlogs.server.match;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "match")
public class Match {

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

    @Column(name = "f_account_id")
    private String friendlyAccountId;

    @Column(name = "o_account_id")
    private String opposingAccountId;

    @Column(name = "f_player")
    private String friendlyPlayer; // the name of the friendly player

    @Column(name = "o_player")
    private String opposingPlayer;

    @Column(name = "first")
    private String first; // the player who went first

    @Column(name = "winner")
    private String winner; // will contain the name of the player who won

    @Column(name = "loser")
    private String loser;

    @Column(name = "quitter")
    private String quitter; // the name of the player who quit if any

    @OneToMany(targetEntity=Turn.class, mappedBy="match")
    private Set<Turn> turns = new HashSet<>();

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
