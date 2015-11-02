package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class AttackChange implements Action, Serializable {

    private Card card;
    private int amount;

    protected AttackChange(Card card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
