package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class Frozen implements Action, Serializable {

    private String side;
    private Card card;
    private boolean frozen;

    public Frozen(String side, Card card, boolean frozen) {
        this.side = side;
        this.card = card;
        this.frozen = frozen;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public int getType() {
        return 11;
    }
}
