package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class ManaOverspent implements Action, Serializable {

    private Card card;
    private int manaOverspent;

    protected ManaOverspent(Card card, int manaOverspent) {
        this.card = card;
        this.manaOverspent = manaOverspent;
    }

    public int getManaOverspent() {
        return manaOverspent;
    }

    public void setManaOverspent(int manaOverspent) {
        this.manaOverspent = manaOverspent;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
