package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class HealthChange implements Action, Serializable {

    private Card card;
    private int amount;

    public HealthChange(Card card, int amount) {
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
