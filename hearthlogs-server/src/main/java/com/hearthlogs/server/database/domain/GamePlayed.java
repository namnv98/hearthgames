package com.hearthlogs.server.database.domain;

import com.hearthlogs.server.util.DurationUtils;

import javax.persistence.*;
import java.time.Duration;
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

    private boolean justAdded;

    private Integer rank;
    private Integer turns;
    private String challenge;

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

    private String friendlyStartingCards;
    private String friendlyMulliganCards;
    @Column(length = 1000)
    private String friendlyDeckCards;

    private String opposingStartingCards;
    private String opposingMulliganCards;
    @Column(length = 1000)
    private String opposingDeckCards;

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

    public Integer getTurns() {
        return turns;
    }

    public void setTurns(Integer turns) {
        this.turns = turns;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getDuration() {
        Duration duration = Duration.between(startTime, endTime);
        return DurationUtils.formatDuration(duration);
    }

    public String getFriendlyStartingCards() {
        return friendlyStartingCards;
    }

    public void setFriendlyStartingCards(String friendlyStartingCards) {
        this.friendlyStartingCards = friendlyStartingCards;
    }

    public String getFriendlyMulliganCards() {
        return friendlyMulliganCards;
    }

    public void setFriendlyMulliganCards(String friendlyMulliganCards) {
        this.friendlyMulliganCards = friendlyMulliganCards;
    }

    public String getFriendlyDeckCards() {
        return friendlyDeckCards;
    }

    public void setFriendlyDeckCards(String friendlyDeckCards) {
        this.friendlyDeckCards = friendlyDeckCards;
    }

    public String getOpposingStartingCards() {
        return opposingStartingCards;
    }

    public void setOpposingStartingCards(String opposingStartingCards) {
        this.opposingStartingCards = opposingStartingCards;
    }

    public String getOpposingMulliganCards() {
        return opposingMulliganCards;
    }

    public void setOpposingMulliganCards(String opposingMulliganCards) {
        this.opposingMulliganCards = opposingMulliganCards;
    }

    public String getOpposingDeckCards() {
        return opposingDeckCards;
    }

    public void setOpposingDeckCards(String opposingDeckCards) {
        this.opposingDeckCards = opposingDeckCards;
    }

    public boolean isJustAdded() {
        return justAdded;
    }

    public void setJustAdded(boolean justAdded) {
        this.justAdded = justAdded;
    }

    public boolean isSameGame(GamePlayed gamePlayed) {
        if (rank != null ? !rank.equals(gamePlayed.rank) : gamePlayed.rank != null) return false;
        if (!turns.equals(gamePlayed.turns)) return false;
        if (!friendlyGameAccountId.equals(gamePlayed.friendlyGameAccountId)) return false;
        if (!opposingGameAccountId.equals(gamePlayed.opposingGameAccountId)) return false;
        if (!winner.equals(gamePlayed.winner)) return false;
        if (!winnerClass.equals(gamePlayed.winnerClass)) return false;
        if (!friendlyStartingCards.equals(gamePlayed.friendlyStartingCards)) return false;
        if (!friendlyMulliganCards.equals(gamePlayed.friendlyMulliganCards)) return false;
        if (!friendlyDeckCards.equals(gamePlayed.friendlyDeckCards)) return false;
        if (!opposingStartingCards.equals(gamePlayed.opposingStartingCards)) return false;
        if (!opposingMulliganCards.equals(gamePlayed.opposingMulliganCards)) return false;
        return opposingDeckCards.equals(gamePlayed.opposingDeckCards);

    }
}
