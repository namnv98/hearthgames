package com.hearthgames.server.solr.domain;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndexedGamePlayed {

    @Id
    @Field(value = "id")
    private String id;

    @Field(value = "friendly_game_account_id_s")
    private String friendlyGameAccountId;
    @Field(value = "friendly_name_s")
    private String friendlyName;
    @Field(value = "friendly_class_s")
    private String friendlyClass;
    @Field(value = "friendly_class_won_b")
    private boolean friendlyClassWon;
    @Field(value = "friendly_class_quit_b")
    private boolean friendlyClassQuit;

    @Field(value = "opposing_game_account_id_s")
    private String opposingGameAccountId;
    @Field(value = "opposing_name_s")
    private String opposingName;
    @Field(value = "opposing_class_s")
    private String opposingClass;
    @Field(value = "opposing_class_won_b")
    private boolean opposingClassWon;
    @Field(value = "opposing_class_quit_b")
    private boolean opposingClassQuit;

    @Field(value = "rank_i")
    private Integer rank;

    @Field(value = "turns_i")
    private int turns;

    @Field(value = "start_time_dt")
    private Date startTime;
    @Field(value = "end_time_dt")
    private Date endTime;

    @Field(value = "friendly_starting_card_ss")
    List<String> friendlyStartingCards = new ArrayList<>();
    @Field(value = "friendly_mulligan_card_ss")
    List<String> friendlyMulliganCards = new ArrayList<>();
    @Field(value = "friendly_deck_card_ss")
    List<String> friendlyDeckCards = new ArrayList<>();

    @Field(value = "opposing_starting_card_ss")
    List<String> opposingStartingCards = new ArrayList<>();
    @Field(value = "opposing_mulligan_card_ss")
    List<String> opposingMulliganCards = new ArrayList<>();
    @Field(value = "opposing_deck_card_ss")
    List<String> opposingDeckCards = new ArrayList<>();

    @Field("matchup_s")
    private String matchup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendlyGameAccountId() {
        return friendlyGameAccountId;
    }

    public void setFriendlyGameAccountId(String friendlyGameAccountId) {
        this.friendlyGameAccountId = friendlyGameAccountId;
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

    public boolean isFriendlyClassWon() {
        return friendlyClassWon;
    }

    public void setFriendlyClassWon(boolean friendlyClassWon) {
        this.friendlyClassWon = friendlyClassWon;
    }

    public boolean isFriendlyClassQuit() {
        return friendlyClassQuit;
    }

    public void setFriendlyClassQuit(boolean friendlyClassQuit) {
        this.friendlyClassQuit = friendlyClassQuit;
    }

    public String getOpposingGameAccountId() {
        return opposingGameAccountId;
    }

    public void setOpposingGameAccountId(String opposingGameAccountId) {
        this.opposingGameAccountId = opposingGameAccountId;
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

    public boolean isOpposingClassWon() {
        return opposingClassWon;
    }

    public void setOpposingClassWon(boolean opposingClassWon) {
        this.opposingClassWon = opposingClassWon;
    }

    public boolean isOpposingClassQuit() {
        return opposingClassQuit;
    }

    public void setOpposingClassQuit(boolean opposingClassQuit) {
        this.opposingClassQuit = opposingClassQuit;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
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

    public List<String> getFriendlyStartingCards() {
        return friendlyStartingCards;
    }

    public void setFriendlyStartingCards(List<String> friendlyStartingCards) {
        this.friendlyStartingCards = friendlyStartingCards;
    }

    public List<String> getFriendlyMulliganCards() {
        return friendlyMulliganCards;
    }

    public void setFriendlyMulliganCards(List<String> friendlyMulliganCards) {
        this.friendlyMulliganCards = friendlyMulliganCards;
    }

    public List<String> getFriendlyDeckCards() {
        return friendlyDeckCards;
    }

    public void setFriendlyDeckCards(List<String> friendlyDeckCards) {
        this.friendlyDeckCards = friendlyDeckCards;
    }

    public List<String> getOpposingStartingCards() {
        return opposingStartingCards;
    }

    public void setOpposingStartingCards(List<String> opposingStartingCards) {
        this.opposingStartingCards = opposingStartingCards;
    }

    public List<String> getOpposingMulliganCards() {
        return opposingMulliganCards;
    }

    public void setOpposingMulliganCards(List<String> opposingMulliganCards) {
        this.opposingMulliganCards = opposingMulliganCards;
    }

    public List<String> getOpposingDeckCards() {
        return opposingDeckCards;
    }

    public void setOpposingDeckCards(List<String> opposingDeckCards) {
        this.opposingDeckCards = opposingDeckCards;
    }


    public String getMatchup() {
        return matchup;
    }

    public void setMatchup(String matchup) {
        this.matchup = matchup;
    }
}