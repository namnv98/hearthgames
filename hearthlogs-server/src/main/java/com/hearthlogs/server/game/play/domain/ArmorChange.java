package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class ArmorChange implements Action, Serializable {

    private Card card;
    private int armor;

    public ArmorChange(Card card, int armor) {
        this.card = card;
        this.armor = armor;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }
}