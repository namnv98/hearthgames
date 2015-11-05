package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class Attached implements Action, Serializable {

    private Card card;
    private Card attachedTo;

    public Attached(Card card, Card attachedTo) {
        this.card = card;
        this.attachedTo = attachedTo;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getAttachedTo() {
        return attachedTo;
    }

    public void setAttachedTo(Card attachedTo) {
        this.attachedTo = attachedTo;
    }
}
