package com.hearthlogs.web.match;

import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String friendlyClass; // the class (i.e. warrior, hunter, etc...)
    @Field
    private String opposingClass;

    @Field
    private List<String> friendlyStartingCards = new ArrayList<>();
    @Field
    private List<String> opposingStartingCards = new ArrayList<>();

    @Field
    private List<String> friendlyMulliganedCards = new ArrayList<>();
    @Field
    private List<String> opposingMulliganedCards = new ArrayList<>();

    @Field
    private List<String> friendlyCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards
    @Field
    private List<String> opposingCards = new ArrayList<>();  // the list of cards that were part of the deck.  This will not include cards created by other cards

    @Field
    private int turns;

    @Field
    private String rank;

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

    public List<String> getFriendlyStartingCards() {
        return friendlyStartingCards;
    }

    public void setFriendlyStartingCards(List<String> friendlyStartingCards) {
        this.friendlyStartingCards = friendlyStartingCards;
    }

    public List<String> getOpposingStartingCards() {
        return opposingStartingCards;
    }

    public void setOpposingStartingCards(List<String> opposingStartingCards) {
        this.opposingStartingCards = opposingStartingCards;
    }

    public List<String> getFriendlyMulliganedCards() {
        return friendlyMulliganedCards;
    }

    public void setFriendlyMulliganedCards(List<String> friendlyMulliganedCards) {
        this.friendlyMulliganedCards = friendlyMulliganedCards;
    }

    public List<String> getOpposingMulliganedCards() {
        return opposingMulliganedCards;
    }

    public void setOpposingMulliganedCards(List<String> opposingMulliganedCards) {
        this.opposingMulliganedCards = opposingMulliganedCards;
    }

    public List<String> getFriendlyCards() {
        return friendlyCards;
    }

    public void setFriendlyCards(List<String> friendlyCards) {
        this.friendlyCards = friendlyCards;
    }

    public List<String> getOpposingCards() {
        return opposingCards;
    }

    public void setOpposingCards(List<String> opposingCards) {
        this.opposingCards = opposingCards;
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
}