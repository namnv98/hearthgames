package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

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

    @Override
    public int getType() {
        return 2;
    }
}
