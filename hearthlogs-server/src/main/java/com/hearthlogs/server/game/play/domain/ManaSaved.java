package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class ManaSaved implements Action, Serializable {

    private Card card;
    private int manaSaved;

    public ManaSaved(Card card, int manaSaved) {
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

    @Override
    public int getType() {
        return 19;
    }
}


