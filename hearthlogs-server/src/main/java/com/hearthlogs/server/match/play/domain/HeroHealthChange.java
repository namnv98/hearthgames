package com.hearthlogs.server.match.play.domain;

import com.hearthlogs.server.match.parse.domain.Card;

import java.io.Serializable;

public class HeroHealthChange implements Action, Serializable {

    private Card card;
    private int health;

    protected HeroHealthChange(Card card, int health) {
        this.card = card;
        this.health = health;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
