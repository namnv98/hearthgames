package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.domain.json.DetachedSerializer;

import java.io.Serializable;

@JsonSerialize(using = DetachedSerializer.class)
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

