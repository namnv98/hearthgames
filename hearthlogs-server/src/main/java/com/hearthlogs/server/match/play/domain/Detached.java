package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class Detached implements Action, Serializable {

    private Card card;
    private Card detachedFrom;

    protected Detached(Card card, Card detachedFrom) {
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

