package com.hearthlogs.server.game.analysis.domain;

import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.game.parse.domain.Card;

import java.util.ArrayList;
import java.util.List;

public class TurnInfo {

    private String turnNumber;
    private String whoseTurn;
    private String turnClass;

    private List<Card> friendlyHand = new ArrayList<>();
    private List<Card> friendlyPlay = new ArrayList<>();
    private List<Card> friendlySecret = new ArrayList<>();
    private List<Card> friendlyWeapon = new ArrayList<>();
    private List<Card> opposingHand = new ArrayList<>();
    private List<Card> opposingPlay = new ArrayList<>();
    private List<Card> opposingSecret = new ArrayList<>();
    private List<Card> opposingWeapon = new ArrayList<>();

    private List<GenericRow> rows = new ArrayList<>();

    public List<Card> getFriendlyHand() {
        return friendlyHand;
    }

    public void setFriendlyHand(List<Card> friendlyHand) {
        this.friendlyHand = friendlyHand;
    }

    public List<Card> getFriendlyPlay() {
        return friendlyPlay;
    }

    public void setFriendlyPlay(List<Card> friendlyPlay) {
        this.friendlyPlay = friendlyPlay;
    }

    public List<Card> getOpposingHand() {
        return opposingHand;
    }

    public void setOpposingHand(List<Card> opposingHand) {
        this.opposingHand = opposingHand;
    }

    public List<Card> getOpposingPlay() {
        return opposingPlay;
    }

    public void setOpposingPlay(List<Card> opposingPlay) {
        this.opposingPlay = opposingPlay;
    }

    public List<Card> getFriendlySecret() {
        return friendlySecret;
    }

    public void setFriendlySecret(List<Card> friendlySecret) {
        this.friendlySecret = friendlySecret;
    }

    public List<Card> getOpposingSecret() {
        return opposingSecret;
    }

    public void setOpposingSecret(List<Card> opposingSecret) {
        this.opposingSecret = opposingSecret;
    }

    public List<Card> getFriendlyWeapon() {
        return friendlyWeapon;
    }

    public void setFriendlyWeapon(List<Card> friendlyWeapon) {
        this.friendlyWeapon = friendlyWeapon;
    }

    public List<Card> getOpposingWeapon() {
        return opposingWeapon;
    }

    public void setOpposingWeapon(List<Card> opposingWeapon) {
        this.opposingWeapon = opposingWeapon;
    }

    public List<GenericRow> getRows() {
        return rows;
    }

    public void setRows(List<GenericRow> rows) {
        this.rows = rows;
    }

    public String getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(String turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(String whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public String getTurnClass() {
        return turnClass;
    }

    public void setTurnClass(String turnClass) {
        this.turnClass = turnClass;
    }
}
