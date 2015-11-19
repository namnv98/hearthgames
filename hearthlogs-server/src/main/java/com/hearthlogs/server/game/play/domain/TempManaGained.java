package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class TempManaGained implements Action, Serializable {

    private Card card;
    private int tempManaGained;

    public TempManaGained(Card card, int tempManaGained) {
        this.card = card;
        this.tempManaGained = tempManaGained;
    }

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

