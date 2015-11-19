package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class HeroHealthChange implements Action, Serializable {

    private Card card;
    private int health;

    public HeroHealthChange(Card card, int health) {
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
