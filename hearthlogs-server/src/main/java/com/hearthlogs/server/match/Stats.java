package com.hearthlogs.server.match;

import org.apache.solr.client.solrj.beans.Field;

import java.util.*;

public class Stats {

    private String id;

    private String winnerClass; // the class (i.e. warrior, hunter, etc...)
    private String loserClass;

    private List<String> winnerCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards
    private List<String> loserCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards

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