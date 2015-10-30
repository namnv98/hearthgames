package com.hearthlogs.server.match.result.action;

import com.hearthlogs.server.match.domain.Card;

public class ArmorChange implements Action {

    private Card card;
    private int health;

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