package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;

import java.io.Serializable;

public class AttackChange implements Action, Serializable {

    private String side;
    private Card card;
    private int amount; // amount of increase
    private int newAttack;

    public AttackChange(String side, Card card, int amount, int newAttack) {
        this.side = side;
        this.card = card;
        this.amount = amount;
        this.newAttack = newAttack;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getNewAttack() {
        return newAttack;
    }

    public void setNewAttack(int newAttack) {
        this.newAttack = newAttack;
    }

    @Override
    public int getType() {
        return 3;
    }
}
