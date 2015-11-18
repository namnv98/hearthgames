package com.hearthlogs.server.match.analysis.domain;

public class VersusInfo {

    private String friendlyName;
    private String opposingName;
    private String friendlyClass;
    private String opposingClass;
    private String winner;
    private String winnerClass;
    private String loser;
    private String loserClass;
    private String quitter;
    private String quitterClass;
    private String friendlyHeroCardId;
    private String opposingHeroCardId;

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getOpposingName() {
        return opposingName;
    }

    public void setOpposingName(String opposingName) {
        this.opposingName = opposingName;
    }

    public String getFriendlyClass() {
        return friendlyClass;
    }

    public void setFriendlyClass(String friendlyClass) {
        this.friendlyClass = friendlyClass;
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

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public String getQuitter() {
        return quitter;
    }

    public void setQuitter(String quitter) {
        this.quitter = quitter;
    }

    public String getWinnerClass() {
        return winnerClass;
    }

    public void setWinnerClass(String winnerClass) {
        this.winnerClass = winnerClass;
    }

    public String getLoserClass() {
        return loserClass;
    }

    public void setLoserClass(String loserClass) {
        this.loserClass = loserClass;
    }

    public String getQuitterClass() {
        return quitterClass;
    }

    public void setQuitterClass(String quitterClass) {
        this.quitterClass = quitterClass;
    }

    public String getFriendlyHeroCardId() {
        return friendlyHeroCardId;
    }

    public void setFriendlyHeroCardId(String friendlyHeroCardId) {
        this.friendlyHeroCardId = friendlyHeroCardId;
    }

    public String getOpposingHeroCardId() {
        return opposingHeroCardId;
    }

    public void setOpposingHeroCardId(String opposingHeroCardId) {
        this.opposingHeroCardId = opposingHeroCardId;
    }
}
