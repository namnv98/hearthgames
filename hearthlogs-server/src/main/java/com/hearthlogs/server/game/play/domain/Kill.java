package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;

import java.io.Serializable;

public class Kill implements Action, Serializable {

    private String kind;
    private String killerSide;
    private String killedSide;
    private Player beneficiary;
    private Card killer;
    private Card killed;
    private boolean favorableTrade;
    private boolean evenTrade;

    public Kill(String kind, String killerSide, String killedSide, Player beneficiary, Card killer, Card killed, boolean favorableTrade, boolean evenTrade) {
        this.kind = kind;
        this.killerSide = killerSide;
        this.killedSide = killedSide;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKillerSide() {
        return killerSide;
    }

    public void setKillerSide(String killerSide) {
        this.killerSide = killerSide;
    }

    public String getKilledSide() {
        return killedSide;
    }

    public void setKilledSide(String killedSide) {
        this.killedSide = killedSide;
    }

    @Override
    public int getType() {
        return 16;
    }
}
