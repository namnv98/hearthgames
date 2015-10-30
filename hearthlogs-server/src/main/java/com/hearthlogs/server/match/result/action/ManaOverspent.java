package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class ManaOverspent implements Action {

    private Card card;
    private int manaOverspent;

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
