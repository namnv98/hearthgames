package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class HeroHealthChange implements Action, Serializable {

    private String side;
    private Card card;
    private int health;

    public HeroHealthChange(String side, Card card, int health) {
        this.side = side;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public int getType() {
        return 13;
    }
}
