package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class Damage implements Action {

    private Card damager;
    private Card damaged;
    private int amount;

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
