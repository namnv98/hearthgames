package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class TempManaGained implements Action {

    private Card card;
    private int tempManaGained;

    public int getTempManaGained() {
        return tempManaGained;
    }

    public void setTempManaGained(int tempManaGained) {
        this.tempManaGained = tempManaGained;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}

