package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class ManaUsed implements Action {

    private Card card;
    private int manaUsed;

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
