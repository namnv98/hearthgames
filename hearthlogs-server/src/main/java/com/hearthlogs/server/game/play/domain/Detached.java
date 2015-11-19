package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class Detached implements Action, Serializable {

    private Card card;
    private Card detachedFrom;

    public Detached(Card card, Card detachedFrom) {
        this.card = card;
        this.detachedFrom = detachedFrom;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getDetachedFrom() {
        return detachedFrom;
    }

    public void setDetachedFrom(Card detachedFrom) {
        this.detachedFrom = detachedFrom;
    }
}

