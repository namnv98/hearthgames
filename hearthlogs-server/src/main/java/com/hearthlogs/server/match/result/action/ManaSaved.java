package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class ManaSaved implements Action {

    private Card card;
    private int manaSaved;

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


