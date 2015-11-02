package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Player;

import java.io.Serializable;

public class Kill implements Action, Serializable {

    private Player beneficiary;
    private Card killer;
    private Card killed;
    private boolean favorableTrade;
    private boolean evenTrade;

    protected Kill(Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        this.beneficiary = beneficiary;
        this.killer = killer;
        this.killed = killed;
        this.favorableTrade = favorableTrade;
        this.evenTrade = evenTrade;
    }

    public Player getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Player beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Card getKiller() {
        return killer;
    }

    public void setKiller(Card killer) {
        this.killer = killer;
    }

    public Card getKilled() {
        return killed;
    }

    public void setKilled(Card killed) {
        this.killed = killed;
    }

    public boolean isFavorableTrade() {
        return favorableTrade;
    }

    public void setFavorableTrade(boolean favorableTrade) {
        this.favorableTrade = favorableTrade;
    }

    public boolean isEvenTrade() {
        return evenTrade;
    }

    public void setEvenTrade(boolean evenTrade) {
        this.evenTrade = evenTrade;
    }
}
