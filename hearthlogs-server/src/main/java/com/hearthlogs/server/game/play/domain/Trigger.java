package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class Trigger implements Action, Serializable {

    private Card card;

    public Trigger(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
