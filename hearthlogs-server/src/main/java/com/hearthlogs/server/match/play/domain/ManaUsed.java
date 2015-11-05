package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class ManaUsed implements Action, Serializable {

    private Card card;
    private int manaUsed;

    public ManaUsed(Card card, int manaUsed) {
        this.card = card;
        this.manaUsed = manaUsed;
    }

    public int getManaUsed() {
        return manaUsed;
    }

    public void setManaUsed(int manaUsed) {
        this.manaUsed = manaUsed;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
