package com.hearthlogs.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.play.domain.json.AttachedSerializer;

import java.io.Serializable;

@JsonSerialize(using = AttachedSerializer.class)
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
