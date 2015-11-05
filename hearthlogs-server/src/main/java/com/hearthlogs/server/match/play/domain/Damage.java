package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class Damage implements Action, Serializable {

    private Card damager;
    private Card damaged;
    private int amount;

    public Damage(Card damager, Card damaged, int amount) {
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
}
