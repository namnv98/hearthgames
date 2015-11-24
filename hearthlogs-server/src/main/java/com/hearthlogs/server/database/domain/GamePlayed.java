package com.hearthlogs.server.database.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GamePlayed {

    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long battletagId;
    private String battletag;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Lob
    @Column(nullable = false)
    private byte[] rawGame;

    private Integer rank;

    @Column(nullable = false)
    private String friendlyGameAccountId;

    @Column(nullable = false)
    private String opposingGameAccountId;

    private String friendlyName;
    private String friendlyClass;
    private String opposingName;
    private String opposingClass;

    private String winner;
    private String winnerClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBattletagId() {
        return battletagId;
    }

    public void setBattletagId(Long battletagId) {
        this.battletagId = battletagId;
    }

    public String getBattletag() {
        return battletag;
    }

    public void setBattletag(String battletag) {
        this.battletag = battletag;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public byte[] getRawGame() {
        return rawGame;
    }

    public void setRawGame(byte[] rawGame) {
        this.rawGame = rawGame;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getFriendlyGameAccountId() {
        return friendlyGameAccountId;
    }

    public void setFriendlyGameAccountId(String friendlyGameAccountId) {
        this.friendlyGameAccountId = friendlyGameAccountId;
    }

    public String getOpposingGameAccountId() {
        return opposingGameAccountId;
    }

    public void setOpposingGameAccountId(String opposingGameAccountId) {
        this.opposingGameAccountId = opposingGameAccountId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyClass() {
        return friendlyClass;
    }

    public void setFriendlyClass(String friendlyClass) {
        this.friendlyClass = friendlyClass;
    }

    public String getOpposingName() {
        return opposingName;
    }

    public void setOpposingName(String opposingName) {
        this.opposingName = opposingName;
    }

    public String getOpposingClass() {
        return opposingClass;
    }

    public void setOpposingClass(String opposingClass) {
        this.opposingClass = opposingClass;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinnerClass() {
        return winnerClass;
    }

    public void setWinnerClass(String winnerClass) {
        this.winnerClass = winnerClass;
    }
}
