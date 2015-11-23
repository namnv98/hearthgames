package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class Damage implements Action, Serializable {

    private String damagerSide;
    private String damagedSide;
    private Card damager;
    private Card damaged;
    private int amount;

    public Damage(String damagerSide, String damagedSide, Card damager, Card damaged, int amount) {
        this.damagerSide = damagerSide;
        this.damagedSide = damagedSide;
        this.damager = damager;
        this.damaged = damaged;
        this.amount = amount;
    }

    public Card getDamager() {
        return damager;
    }

    public void setDamager(Card damager) {
        this.damager = damager;
    }

    public Card getDamaged() {
        return damaged;
    }

    public void setDamaged(Card damaged) {
        this.damaged = damaged;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDamagerSide() {
        return damagerSide;
    }

    public void setDamagerSide(String damagerSide) {
        this.damagerSide = damagerSide;
    }

    public String getDamagedSide() {
        return damagedSide;
    }

    public void setDamagedSide(String damagedSide) {
        this.damagedSide = damagedSide;
    }

    @Override
    public int getType() {
        return 8;
    }
}
