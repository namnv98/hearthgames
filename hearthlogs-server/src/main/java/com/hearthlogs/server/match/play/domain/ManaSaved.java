package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class ManaSaved implements Action, Serializable {

    private Card card;
    private int manaSaved;

    protected ManaSaved(Card card, int manaSaved) {
        this.card = card;
        this.manaSaved = manaSaved;
    }

    public int getManaSaved() {
        return manaSaved;
    }

    public void setManaSaved(int manaSaved) {
        this.manaSaved = manaSaved;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}


