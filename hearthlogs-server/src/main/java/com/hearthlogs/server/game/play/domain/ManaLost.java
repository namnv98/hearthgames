package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class ManaLost implements Action, Serializable {

    private Card card;
    private int manaLost;

    public ManaLost(Card card, int manaLost) {
        this.card = card;
        this.manaLost = manaLost;
    }

    public int getManaLost() {
        return manaLost;
    }

    public void setManaLost(int manaLost) {
        this.manaLost = manaLost;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
