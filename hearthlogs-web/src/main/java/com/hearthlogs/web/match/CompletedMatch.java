package com.hearthlogs.web.match;

import org.apache.solr.client.solrj.beans.Field;

import java.util.*;

public class CompletedMatch {

    @Field
    private String id;

    @Field
    private String friendlyAccountId;

    @Field
    private String opposingAccountId;

    @Field
    private Date startTime;

    @Field
    private Date endTime;

    @Field
    private String friendlyPlayer; // the name of the friendly player

    @Field
    private String opposingPlayer;

    @Field
    private String firstPlayer; // the player who went first

    @Field
    private String winner; // will contain the name of the player who won
    @Field
    private String loser;
    @Field
    private String quitter; // the name of the player who quit if any

    @Field
    private String winnerClass; // the class (i.e. warrior, hunter, etc...)
    @Field
    private String loserClass;

    @Field
    private List<String> winnerCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards
    @Field
    private List<String> loserCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards

    @Field
    private int turns;

    @Field
    private String rank;

    @Field
    private float winnerManaEfficiency;

    @Field
    private int winnerManaGained;

    @Field
    private int winnerManaUsed;

    @Field
    private double winnerBoardControl;

    @Field
    private int winnerFavorableTrades;

    @Field
    private int winnerCardAdvantage;

    @Field
    private float loserManaEfficiency;

    @Field
    private int loserManaGained;

    @Field
    private int loserManaUsed;

    @Field
    private double loserBoardControl;

    @Field
    private int loserFavorableTrades;

    @Field
    private int loserCardAdvantage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendlyAccountId() {
        return friendlyAccountId;
    }

    public void setFriendlyAccountId(String friendlyAccountId) {
        this.friendlyAccountId = friendlyAccountId;
    }

    public String getOpposingAccountId() {
        return opposingAccountId;
    }

    public void setOpposingAccountId(String opposingAccountId) {
        this.opposingAccountId = opposingAccountId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFriendlyPlayer() {
        return friendlyPlayer;
    }

    public void setFriendlyPlayer(String friendlyPlayer) {
        this.friendlyPlayer = friendlyPlayer;
    }

    public String getOpposingPlayer() {
        return opposingPlayer;
    }

    public void setOpposingPlayer(String opposingPlayer) {
        this.opposingPlayer = opposingPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
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

    public List<String> getWinnerCards() {
        return winnerCards;
    }

    public void setWinnerCards(List<String> winnerCards) {
        this.winnerCards = winnerCards;
    }

    public List<String> getLoserCards() {
        return loserCards;
    }

    public void setLoserCards(List<String> loserCards) {
        this.loserCards = loserCards;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public float getWinnerManaEfficiency() {
        return winnerManaEfficiency;
    }

    public void setWinnerManaEfficiency(float winnerManaEfficiency) {
        this.winnerManaEfficiency = winnerManaEfficiency;
    }

    public int getWinnerManaGained() {
        return winnerManaGained;
    }

    public void setWinnerManaGained(int winnerManaGained) {
        this.winnerManaGained = winnerManaGained;
    }

    public int getWinnerManaUsed() {
        return winnerManaUsed;
    }

    public void setWinnerManaUsed(int winnerManaUsed) {
        this.winnerManaUsed = winnerManaUsed;
    }

    public double getWinnerBoardControl() {
        return winnerBoardControl;
    }

    public void setWinnerBoardControl(double winnerBoardControl) {
        this.winnerBoardControl = winnerBoardControl;
    }

    public int getWinnerFavorableTrades() {
        return winnerFavorableTrades;
    }

    public void setWinnerFavorableTrades(int winnerFavorableTrades) {
        this.winnerFavorableTrades = winnerFavorableTrades;
    }

    public int getWinnerCardAdvantage() {
        return winnerCardAdvantage;
    }

    public void setWinnerCardAdvantage(int winnerCardAdvantage) {
        this.winnerCardAdvantage = winnerCardAdvantage;
    }

    public float getLoserManaEfficiency() {
        return loserManaEfficiency;
    }

    public void setLoserManaEfficiency(float loserManaEfficiency) {
        this.loserManaEfficiency = loserManaEfficiency;
    }

    public int getLoserManaGained() {
        return loserManaGained;
    }

    public void setLoserManaGained(int loserManaGained) {
        this.loserManaGained = loserManaGained;
    }

    public int getLoserManaUsed() {
        return loserManaUsed;
    }

    public void setLoserManaUsed(int loserManaUsed) {
        this.loserManaUsed = loserManaUsed;
    }

    public double getLoserBoardControl() {
        return loserBoardControl;
    }

    public void setLoserBoardControl(double loserBoardControl) {
        this.loserBoardControl = loserBoardControl;
    }

    public int getLoserFavorableTrades() {
        return loserFavorableTrades;
    }

    public void setLoserFavorableTrades(int loserFavorableTrades) {
        this.loserFavorableTrades = loserFavorableTrades;
    }

    public int getLoserCardAdvantage() {
        return loserCardAdvantage;
    }

    public void setLoserCardAdvantage(int loserCardAdvantage) {
        this.loserCardAdvantage = loserCardAdvantage;
    }
}